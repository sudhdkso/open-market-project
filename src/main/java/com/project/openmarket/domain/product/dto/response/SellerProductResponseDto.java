package com.project.openmarket.domain.product.dto.response;

import com.project.openmarket.domain.product.entity.Product;

public record SellerProductResponseDto(String id, String name, int price, int stock) {
	public static SellerProductResponseDto of(Product product) {
		return new SellerProductResponseDto(product.getId().toHexString(), product.getName(), product.getPrice(),
			product.getStock());
	}
}
