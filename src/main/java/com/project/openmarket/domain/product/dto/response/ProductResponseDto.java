package com.project.openmarket.domain.product.dto.response;

import com.project.openmarket.domain.product.entity.Product;

public record ProductResponseDto(String name, int price, int stock) {
	public static ProductResponseDto of(Product product){
		return new ProductResponseDto(product.getName(), product.getPrice(), product.getStock());
	}
}
