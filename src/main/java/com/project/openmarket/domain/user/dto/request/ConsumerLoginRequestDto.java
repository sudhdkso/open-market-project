package com.project.openmarket.domain.user.dto.request;

import com.project.openmarket.validator.Validator;

import jakarta.validation.constraints.NotEmpty;

public record ConsumerLoginRequestDto(
	@NotEmpty String email,
	@NotEmpty String password) {
	public ConsumerLoginRequestDto {
		Validator.validateEmail(email);
	}
}
