package com.project.openmarket.domain.product.dto.response;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.dto.reposne.UserSimpleResponseDto;

public record ProductResponseDto(String id, String name, String description, int price, double avgRating,
								 UserSimpleResponseDto seller) {
	public static ProductResponseDto of(Product product) {
		return new ProductResponseDto(product.getId().toHexString(), product.getName(), product.getDescription(),
			product.getPrice(),
			product.getAvgRating(), UserSimpleResponseDto.of(product.getSeller()));
	}
}