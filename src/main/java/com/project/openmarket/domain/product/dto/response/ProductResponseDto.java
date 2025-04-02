package com.project.openmarket.domain.product.dto.response;

import org.bson.types.ObjectId;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Seller;

public record ProductResponseDto(String id, String name, int price, double avgScore, Seller seller) {
	public static ProductResponseDto of(Product product){
		return new ProductResponseDto(product.getId().toHexString(), product.getName(), product.getPrice(), product.getAvgScore(), product.getSeller());
	}
}