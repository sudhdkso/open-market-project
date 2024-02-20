package com.project.openmarket.domain.user.dto.request;

import com.project.openmarket.validator.Validator;

public record ConsumerLoginRequestDto(String email, String password) {
	public ConsumerLoginRequestDto {
		Validator.validateEmail(email);
	}
}
