package com.project.openmarket.domain.product.dto.response;

import java.util.List;

import com.project.openmarket.domain.product.entity.Product;

public record SellerProductResponsesDto(List<SellerProductResponseDto> products) {
	public static SellerProductResponsesDto of(List<Product> products) {
		return new SellerProductResponsesDto(
			products.stream()
				.map(SellerProductResponseDto::of)
				.toList()
		);
	}
}
