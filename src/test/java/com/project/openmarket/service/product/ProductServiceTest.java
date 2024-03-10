package com.project.openmarket.service.product;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.dto.request.ProductUpdateReqeustDto;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.service.ServiceTestMock;

class ProductServiceTest extends ServiceTestMock {
	@InjectMocks
	ProductService productService;

	@Test
	@DisplayName("판매자에게 같은 이름의 상품이 존재하면 예외가 발생한다.")
	void existsByNameAndSeller(){
		given(productRepository.existsByNameAndSeller(anyString(),any(Seller.class)))
			.willReturn(true);

		assertThatThrownBy(() -> productService.create(createProduct(""),seller))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ALREADY_EXISTS_PRODUCT.getMessage());
	}

	@Test
	@DisplayName("상품의 내용을 업데이트하려는데 상품의 id로 상품을 찾을 수 없으면 예외가 발생한다.")
	void notFoundWillUpdateProduct(){
		given(productRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		assertThatThrownBy(() -> productService.update(updateProduct("일품"),seller))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(NOT_FOUND_PRODUCT.getMessage());
	}

	@Test
	@DisplayName("상품의 이름을 업데이트하려는데 판매자에게 이미 존재하는 상품명이면 예외가 발생한다.")
	void existsByUpdateNameAndSeller(){
		given(productRepository.findById(anyLong()))
			.willReturn(Optional.of(Product.of(createProduct("상품"), seller)));
		given(productRepository.existsByNameAndSeller(anyString(), any(Seller.class)))
			.willReturn(true);

		assertThatThrownBy(() -> productService.update(updateProduct("일품"),seller))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ALREADY_EXISTS_PRODUCT.getMessage());
	}

	@Test
	@DisplayName("존재하지 않는 상품의 id로 상품 정보를 요청하면 예외가 발생한다.")
	void findProductByInvalidId(){
		given(productRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		assertThatThrownBy(() -> productService.findById(0L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(NOT_FOUND_PRODUCT.getMessage());
	}

	ProductRequestDto createProduct(String name){
		return new ProductRequestDto(name, 1000, 10);
	}

	ProductUpdateReqeustDto updateProduct(String name){
		return new ProductUpdateReqeustDto(1L, name, 1000, 10);
	}
}
