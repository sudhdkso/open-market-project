package com.project.openmarket.domain.user.dto.reposne;

import com.project.openmarket.domain.user.entity.User;

public record UserResponseDto(String email, String name, String phoneNumber) {
	public static UserResponseDto of(User user){
		return new UserResponseDto(user.getEmail(), user.getName(), user.getPhoneNumber());
	}

}
