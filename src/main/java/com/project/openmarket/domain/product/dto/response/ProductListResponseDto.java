package com.project.openmarket.domain.product.dto.response;

import com.project.openmarket.domain.product.entity.Product;

public record ProductListResponseDto(String id, String name, int price) {
	public static ProductListResponseDto of(Product product){
		return new ProductListResponseDto(product.getId().toHexString(), product.getName(), product.getPrice());
	}
}
