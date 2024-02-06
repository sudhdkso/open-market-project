package com.project.openmarket.domain.user.dto.reposne;

import com.project.openmarket.domain.user.entity.User;

public record UserCreateResponseDto(String email, String name, String phoneNumber) {

	public static UserCreateResponseDto of(User user){
		return new UserCreateResponseDto(user.getEmail(), user.getName(), user.getPhoneNumber());
	}

}
