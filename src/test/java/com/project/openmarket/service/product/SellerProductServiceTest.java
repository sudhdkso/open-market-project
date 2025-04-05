package com.project.openmarket.service.product;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.dto.request.ProductUpdateReqeustDto;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.SellerProductService;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.service.ServiceTestMock;

@ExtendWith(MockitoExtension.class)
public class SellerProductServiceTest extends ServiceTestMock {

	@InjectMocks
	SellerProductService sellerProductService;

	@DisplayName("판매자에게 같은 이름의 상품이 존재하면 예외가 발생한다.")
	@Test
	void whenProductNameExistsForSeller_thenThrowException() {
		given(productRepository.existsByNameAndSeller(any(), any(Seller.class)))
			.willReturn(true);

		assertThatThrownBy(() -> sellerProductService.create(createProduct("product"), seller))
			.isInstanceOf(CustomException.class)
			.hasMessage(ALREADY_EXISTS_PRODUCT.getMessage());
	}

	@DisplayName("판매자에게 없는 상품 이름과 판매자가 존재하면 상품 등록에 성공한다.")
	@Test
	void whenProductNameIsUniqueForSeller_thenCreateProductSuccess() {
		final var request = createProduct("상품");
		Product savedProduct = spy(Product.of(request, seller));

		given(productRepository.save(any(Product.class))).willReturn(savedProduct);
		given(productRepository.existsByNameAndSeller(anyString(), any(Seller.class))).willReturn(false);

		ObjectId id = new ObjectId("67ea40df5aeb2844f05b84e8");
		given(savedProduct.getId()).willReturn(id);

		assertThatNoException()
			.isThrownBy(() -> sellerProductService.create(request, seller));

		verify(productRepository, times(1)).save(any(Product.class));
	}

	@Nested
	@DisplayName("상품 업데이트 시 ")
	class updateProduct {
		@DisplayName("상품의 id로 상품을 찾을 수 없으면 예외가 발생한다.")
		@Test
		void whenProductIdIsNotFound_thenThrowExceptionOnUpdate() {
			given(productRepository.findByIdWithLock(any()))
				.willReturn(Optional.empty());

			assertThatThrownBy(
				() -> sellerProductService.update("67ec1324da973979b3723d17", updateProduct("일품"), seller))
				.isInstanceOf(CustomException.class)
				.hasMessage(NOT_FOUND_PRODUCT.getMessage());
		}

		@DisplayName("판매자에게 이미 존재하는 상품명이면 예외가 발생한다.")
		@Test
		void whenProductNameAlreadyExistsForSeller_thenThrowExceptionOnUpdate() {
			given(productRepository.findByIdWithLock(any()))
				.willReturn(Optional.of(product));
			given(productRepository.existsByNameAndSeller(anyString(), any(Seller.class)))
				.willReturn(true);
			String productId = "67ec1324da973979b3723d17";

			assertThatThrownBy(() -> sellerProductService.update(productId, updateProduct("일품"), seller))
				.isInstanceOf(CustomException.class)
				.hasMessage(ALREADY_EXISTS_PRODUCT.getMessage());
		}

	}

	@DisplayName("product가 존재할 때 product삭제에 성공한다.")
	@Test
	void whenProductExists_thenDeleteSuccess() {
		//given
		String productId = "67ea40df5aeb2844f05b84e8";
		given(productRepository.findByIdWithLock(any())).willReturn(Optional.of(product));
		doNothing().when(productRepository).delete(any(Product.class));

		//when
		sellerProductService.delete(productId);

		//then
		verify(productRepository, times(1)).delete(any(Product.class));
	}

	@DisplayName("product가 존재하지 않을때 product삭제에 실패한다.")
	@Test
	void whenProductDoesNotExist_thenDeleteFails() {
		//given
		String productId = "67ea40df5aeb2844f05b84e8";
		given(productRepository.findByIdWithLock(any())).willReturn(Optional.empty());

		//when & then
		assertThatThrownBy(() -> sellerProductService.delete(productId))
			.isInstanceOf(CustomException.class)
			.hasMessage(NOT_FOUND_PRODUCT.getMessage());
	}

	@DisplayName("판매자가 등록한 모든 상품을 조회하면 해당 상품 목록을 반환한다.")
	@Test
	void whenSellerExists_thenReturnAllProductsBySeller() {
		//given
		Product product1 = new Product("product 1", "descrption", 1000, 1, seller);
		Product product2 = new Product("product 2", "descrption", 1000, 1, seller);
		List<Product> products = List.of(product1, product2);

		given(productRepository.findBySeller(any())).willReturn(products);
		//when
		List<Product> response = sellerProductService.findAllBySeller(seller);
		//then
		assertThat(response.size()).isEqualTo(products.size());
		assertThat(response.get(0)).isEqualTo(product1);
		assertThat(response.get(1)).isEqualTo(product2);

		verify(productRepository, times(1)).findBySeller(any(Seller.class));
	}

	ProductRequestDto createProduct(String name) {
		return new ProductRequestDto(name, "", 1000, 10);
	}

	ProductUpdateReqeustDto updateProduct(String name) {
		return new ProductUpdateReqeustDto(name, "", 1000, 10);
	}

}
