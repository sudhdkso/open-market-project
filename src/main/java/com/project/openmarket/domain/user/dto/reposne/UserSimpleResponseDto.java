package com.project.openmarket.domain.user.dto.reposne;

import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;

public record UserSimpleResponseDto(String id, String email, String name) {
	public static UserSimpleResponseDto of(Seller seller) {
		return new UserSimpleResponseDto(seller.getId().toHexString(), seller.getEmail(), seller.getName());
	}

	public static UserSimpleResponseDto of(Consumer consumer) {
		return new UserSimpleResponseDto(consumer.getId().toHexString(), consumer.getEmail(), consumer.getName());
	}
}
