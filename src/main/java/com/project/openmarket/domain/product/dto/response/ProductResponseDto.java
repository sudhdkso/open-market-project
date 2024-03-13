package com.project.openmarket.domain.product.dto.response;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Seller;

public record ProductResponseDto(Long id, String name, int price, int stock, Seller seller) {
	public static ProductResponseDto of(Product product){
		return new ProductResponseDto(product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getSeller());
	}
}
