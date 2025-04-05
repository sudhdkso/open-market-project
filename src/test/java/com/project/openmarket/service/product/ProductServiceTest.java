package com.project.openmarket.service.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.dto.request.ProductUpdateReqeustDto;
import com.project.openmarket.domain.product.dto.response.ProductListResponsesDto;
import com.project.openmarket.domain.product.dto.response.ProductResponseDto;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.service.ServiceTestMock;

class ProductServiceTest extends ServiceTestMock {
	@InjectMocks
	ProductService productService;

	@Nested
	@DisplayName("상품 조회 시 ")
	class findProduct {
		@DisplayName("존재하는 상품의 id로 상품의 정보를 요청하면 성공한다.")
		@Test
		void whenProductIdIsValid_thenReturnProduct() {
			given(productRepository.getById(any()))
				.willReturn(product);
			ObjectId id = new ObjectId("67ea40df5aeb2844f05b84e8");
			given(product.getId()).willReturn(id);

			given(product.getSeller()).willReturn(seller);
			given(seller.getId()).willReturn(new ObjectId("67ea40df5aeb2844f05b84e8"));
			given(seller.getName()).willReturn("판매자");

			assertThatNoException()
				.isThrownBy(() -> productService.findById(id.toHexString()));
		}

		@DisplayName("존재하는 상품명으로 리뷰순으로 상품을 조회할 수 있다.")
		@Test
		void whenSearchByName_thenFindProductByScore() {
			given(productRepository.findByNameContainsOrderByAvgScoreDesc(anyString()))
				.willReturn(List.of(product));
			assertThatNoException()
				.isThrownBy(() -> productService.findByScoreDesc("상품1"));

			verify(productRepository, times(1)).findByNameContainsOrderByAvgScoreDesc(anyString());
		}
	}

	@DisplayName("상품의 재고를 증가시킬 수 있다.")
	@Test
	void whenIncreaseStock_thenSuccess() {
		//given
		int expectedCount = 1;
		//when
		assertThatNoException()
			.isThrownBy(() -> productService.increaseProductStock(1, product));
		//then
		verify(product, times(1)).increaseStock(expectedCount);
		verify(productRepository, times(1)).save(any(Product.class));
	}

	@DisplayName("상품의 재고를 감소시킬 수 있다.")
	@Test
	void whenDecreaseStock_thenSuccess() {
		//given
		int expectedStock = 1;
		//when
		assertThatNoException()
			.isThrownBy(() -> productService.decreaseProductStock(1, product));
		//then
		verify(product, times(1)).decreaseStock(expectedStock);
		verify(productRepository, times(1)).save(any(Product.class));
	}

	@DisplayName("상품의 평균 평점을 업데이트할 수 있다.")
	@Test
	void whenUpdateReviewScore_thenSuccess() {
		assertThatNoException()
			.isThrownBy(() -> productService.updateProductAvgScore(4.0, product));

		verify(product, times(1)).updateAvgScore(anyDouble());
		verify(productRepository, times(1)).save(any(Product.class));
	}

	@DisplayName("저장된 상품이 있을때 모든 상품을 조회할 수 있다.")
	@Test
	void whenProductsExist_thenReturnAllProducts() {
		//given
		Product product1 = spy(new Product("1", "", 1000, 10, seller));
		Product product2 = spy(new Product("2", "", 1000, 10, seller));
		List<Product> products = List.of(product1, product2);

		given(productRepository.findAll()).willReturn(products);
		given(product1.getId()).willReturn(new ObjectId("67ea40df5aeb2844f05b84e8"));
		given(product2.getId()).willReturn(new ObjectId("67ec1324da973979b3723d17"));

		//when
		ProductListResponsesDto result = productService.findAllProduct();

		//then
		assertThat(result.products().size()).isEqualTo(products.size());
		assertThat(result.products().get(0).name()).isEqualTo(product1.getName());

		verify(productRepository, times(1)).findAll();
	}

	@DisplayName("물건이름으로 검색할때 페이징하여 물건들을 조회할 수 있다.")
	@Test
	void whenSearchByName_thenReturnPagedProducts() {
		//given
		String name = "Laptop";
		Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Order.asc("name")));

		Product product1 = spy(new Product("Laptop Pro", "", 12000, 1, seller));
		Product product2 = spy(new Product("Laptop Air", "", 999, 2, seller));

		List<Product> content = List.of(product1, product2);

		Page<Product> productPage = new PageImpl<>(content, pageable, 5);

		given(productRepository.findByNameContaining(name, pageable)).willReturn(productPage);
		given(product1.getId()).willReturn(new ObjectId("67ea40df5aeb2844f05b84e8"));
		given(product2.getId()).willReturn(new ObjectId("67ec1324da973979b3723d17"));

		given(seller.getId()).willReturn(new ObjectId("67ea40df5aeb2844f05b84e8"));
		given(seller.getName()).willReturn("판매자");
		//when
		Page<ProductResponseDto> result = productService.findProductByName(name, pageable);

		//then
		assertThat(result.getTotalElements()).isEqualTo(5);
		assertThat(result.getTotalPages()).isEqualTo(3);
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent().get(0).name()).isEqualTo("Laptop Pro");

		verify(productRepository, times(1)).findByNameContaining(name, pageable);
	}

	ProductRequestDto createProduct(String name) {
		return new ProductRequestDto(name, "", 1000, 10);
	}

	ProductUpdateReqeustDto updateProduct(String name) {
		return new ProductUpdateReqeustDto(name, "", 1000, 10);
	}
}
