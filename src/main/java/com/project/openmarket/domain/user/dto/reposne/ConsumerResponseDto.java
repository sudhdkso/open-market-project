package com.project.openmarket.domain.user.dto.reposne;

import com.project.openmarket.domain.user.entity.Consumer;

public record ConsumerResponseDto(String email, String name, String phoneNumber, String address, Long cache, Long point) {
	public static ConsumerResponseDto of(Consumer consumer){
		return new ConsumerResponseDto(consumer.getEmail(), consumer.getName(), consumer.getPhoneNumber(), consumer.getAddress(), consumer.getCache(), consumer.getPoint());
	}
}
