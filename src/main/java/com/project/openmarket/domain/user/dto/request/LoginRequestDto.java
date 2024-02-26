package com.project.openmarket.domain.user.dto.request;

import com.project.openmarket.global.validator.Validator;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDto(
	@NotEmpty String email,
	@NotEmpty String password) {
	public LoginRequestDto {
		Validator.validateEmail(email);
	}
}
