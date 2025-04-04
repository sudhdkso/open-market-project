package com.project.openmarket.service.product;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

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
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.service.ServiceTestMock;

class ProductServiceTest extends ServiceTestMock {
	@InjectMocks
	ProductService productService;

	@Test
	@DisplayName("판매자에게 같은 이름의 상품이 존재하면 예외가 발생한다.")
	void existsByNameAndSeller() {
		given(productRepository.existsByNameAndSeller(anyString(), any(Seller.class)))
			.willReturn(true);

		assertThatThrownBy(() -> productService.create(createProduct(""), seller))
			.isInstanceOf(CustomException.class)
			.hasMessage(ALREADY_EXISTS_PRODUCT.getMessage());
	}

	@Nested
	@DisplayName("상품 업데이트 시 ")
	class updateProduct {
		@Test
		@DisplayName("상품의 id로 상품을 찾을 수 없으면 예외가 발생한다.")
		void notFoundWillUpdateProduct() {
			given(productRepository.findByIdWithLock(any()))
				.willReturn(Optional.empty());

			assertThatThrownBy(() -> productService.update("67ec1324da973979b3723d17", updateProduct("일품"), seller))
				.isInstanceOf(CustomException.class)
				.hasMessage(NOT_FOUND_PRODUCT.getMessage());
		}

		@Test
		@DisplayName("판매자에게 이미 존재하는 상품명이면 예외가 발생한다.")
		void existsByUpdateNameAndSeller() {
			given(productRepository.findByIdWithLock(any()))
				.willReturn(Optional.of(Product.of(createProduct("상품"), seller)));
			given(productRepository.existsByNameAndSeller(anyString(), any(Seller.class)))
				.willReturn(true);
			String productId = "67ec1324da973979b3723d17";

			assertThatThrownBy(() -> productService.update(productId, updateProduct("일품"), seller))
				.isInstanceOf(CustomException.class)
				.hasMessage(ALREADY_EXISTS_PRODUCT.getMessage());
		}

	}

	@Nested
	@DisplayName("상품 조회 시 ")
	class findProduct {
		@Test
		@DisplayName("존재하는 상품의 id로 상품의 정보를 요청하면 성공한다.")
		void findProductById() {
			given(productRepository.getById(any()))
				.willReturn(product);
			ObjectId id = new ObjectId("67ea40df5aeb2844f05b84e8");
			given(product.getId()).willReturn(id);
			assertThatNoException()
				.isThrownBy(() -> productService.findById(id.toHexString()));
		}

		@Test
		@DisplayName("존재하는 상품명으로 리뷰순으로 상품을 조회할 수 있다.")
		void findProductByScore() {
			given(productRepository.findByNameContainsOrderByAvgScoreDesc(anyString()))
				.willReturn(List.of(product));
			assertThatNoException()
				.isThrownBy(() -> productService.findByScoreDesc("상품1"));

			then(productRepository)
				.should(times(1))
				.findByNameContainsOrderByAvgScoreDesc(anyString());
		}
	}

	@Test
	@DisplayName("상품 재고 증가 테스트")
	void increaseProductStockTest() {
		int expectedCount = 1;
		assertThatNoException()
			.isThrownBy(() -> productService.increaseProductStock(1, product));

		then(product)
			.should(times(1))
			.increaseStock(expectedCount);

		then(productRepository)
			.should(times(1))
			.save(any(Product.class));

	}

	@Test
	@DisplayName("상품 리뷰 업데이트 테스트")
	void updateProductAvgScore() {
		assertThatNoException()
			.isThrownBy(() -> productService.updateProductAvgScore(4.0, product));

		then(product)
			.should(times(1))
			.updateAvgScore(anyDouble());

		then(productRepository)
			.should(times(1))
			.save(any(Product.class));
	}

	@Test
	@DisplayName("상품 재고 감소 테스트")
	void decreaseProductStockTest() {
		int expectedCount = 1;
		assertThatNoException()
			.isThrownBy(() -> productService.decreaseProductStock(1, product));

		then(product)
			.should(times(1))
			.decreaseStock(expectedCount);

		then(productRepository)
			.should(times(1))
			.save(any(Product.class));

	}

	@Test
	@DisplayName("저장된 상품이 있을때 모든 상품을 조회할 수 있다.")
	void findAllProductTest() {
		//given
		Product product1 = spy(new Product("1", 1000, 10, seller));
		Product product2 = spy(new Product("2", 1000, 10, seller));
		List<Product> products = List.of(product1, product2);

		given(productRepository.findAll()).willReturn(products);
		given(product1.getId()).willReturn(new ObjectId("67ea40df5aeb2844f05b84e8"));
		given(product2.getId()).willReturn(new ObjectId("67ec1324da973979b3723d17"));

		//when
		ProductListResponsesDto result = productService.findAllProduct();

		//then
		assertThat(result.products().size()).isEqualTo(products.size());
		assertThat(result.products().get(0).name()).isEqualTo(product1.getName());

		then(productRepository)
			.should(times(1))
			.findAll();
	}

	@Test
	@DisplayName("물건이름으로 검색할때 페이징하여 물건들을 조회할 수 있다.")
	void findProductByNameTest() {
		//given
		String name = "Laptop";
		Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Order.asc("name")));

		Product product1 = spy(new Product("Laptop Pro", 12000, 1, seller));
		Product product2 = spy(new Product("Laptop Air", 999, 2, seller));

		List<Product> content = List.of(product1, product2);

		Page<Product> productPage = new PageImpl<>(content, pageable, 5);

		given(productRepository.findByNameContaining(name, pageable)).willReturn(productPage);
		given(product1.getId()).willReturn(new ObjectId("67ea40df5aeb2844f05b84e8"));
		given(product2.getId()).willReturn(new ObjectId("67ec1324da973979b3723d17"));

		//when
		Page<ProductResponseDto> result = productService.findProductByName(name, pageable);

		//then
		assertThat(result.getTotalElements()).isEqualTo(5);
		assertThat(result.getTotalPages()).isEqualTo(3);
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent().get(0).name()).isEqualTo("Laptop Pro");

		then(productRepository)
			.should(times((1)))
			.findByNameContaining(name, pageable);
	}

	@Test
	@DisplayName("product가 존재할 때 product삭제에 성공한다.")
	void deleteValidProductTest_Success() {
		//given
		String productId = "67ea40df5aeb2844f05b84e8";
		given(productRepository.findByIdWithLock(any())).willReturn(Optional.of(product));
		doNothing().when(productRepository).delete(any(Product.class));

		//when
		productService.delete(productId);

		//then
		then(productRepository)
			.should(times(1))
			.delete(any(Product.class));
	}

	@Test
	@DisplayName("product가 존재하지 않을때 product삭제에 실패한다.")
	void deleteInvalidProductTest_Fail() {
		//given
		String productId = "67ea40df5aeb2844f05b84e8";
		given(productRepository.findByIdWithLock(any())).willReturn(Optional.empty());

		//when & then
		assertThatThrownBy(() -> productService.delete(productId))
			.isInstanceOf(CustomException.class)
			.hasMessage(NOT_FOUND_PRODUCT.getMessage());
	}

	ProductRequestDto createProduct(String name) {
		return new ProductRequestDto(name, 1000, 10);
	}

	ProductUpdateReqeustDto updateProduct(String name) {
		return new ProductUpdateReqeustDto(name, 1000, 10);
	}
}
