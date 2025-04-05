package com.project.openmarket.domain.user.dto.reposne;

import com.project.openmarket.domain.user.entity.Seller;

public record SellerResponseDto(String id, String name) {
	public static SellerResponseDto of(Seller seller) {
		return new SellerResponseDto(seller.getId().toHexString(), seller.getName());
	}
}
