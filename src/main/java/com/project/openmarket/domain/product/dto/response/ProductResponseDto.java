package com.project.openmarket.domain.product.dto.response;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.dto.reposne.SellerResponseDto;

public record ProductResponseDto(String id, String name, int price, double avgRating, SellerResponseDto seller) {
	public static ProductResponseDto of(Product product) {
		return new ProductResponseDto(product.getId().toHexString(), product.getName(), product.getPrice(),
			product.getAvgRating(), SellerResponseDto.of(product.getSeller()));
	}
}