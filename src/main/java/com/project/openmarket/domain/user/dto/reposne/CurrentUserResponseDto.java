package com.project.openmarket.domain.user.dto.reposne;

import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;

public record CurrentUserResponseDto(String id, String email, String name, String phoneNumber, Long cash, String role) {

	public static CurrentUserResponseDto ofSeller(Seller seller){
		return new CurrentUserResponseDto(seller.getId().toHexString(), seller.getEmail(), seller.getName(), seller.getPhoneNumber(), seller.getCash(), "seller");
	}

	public static CurrentUserResponseDto ofConsumer(Consumer consumer){
		return new CurrentUserResponseDto(consumer.getId().toHexString(), consumer.getEmail(), consumer.getName(), consumer.getPhoneNumber(), consumer.getCash(), "consumer");
	}

}
