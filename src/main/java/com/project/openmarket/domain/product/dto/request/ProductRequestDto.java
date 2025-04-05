package com.project.openmarket.domain.product.dto.request;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Seller;

public record ProductRequestDto(String name, String description, int price, int stock) {
	public Product toEntity(Seller seller) {
		return new Product(name, description, price, stock, seller);
	}
}
