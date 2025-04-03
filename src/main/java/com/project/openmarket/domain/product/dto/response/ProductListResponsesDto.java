package com.project.openmarket.domain.product.dto.response;

import java.util.List;

import com.project.openmarket.domain.product.entity.Product;

public record ProductListResponsesDto(List<ProductListResponseDto> products) {
	public static ProductListResponsesDto of(List<Product> products){
		return new ProductListResponsesDto(products.stream()
			.map(ProductListResponseDto::of)
			.toList());
	}
}
