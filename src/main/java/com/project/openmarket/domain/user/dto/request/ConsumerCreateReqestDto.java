package com.project.openmarket.domain.user.dto.request;

import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.validator.Validator;

import jakarta.validation.constraints.NotEmpty;

public record ConsumerCreateReqestDto(
	@NotEmpty String email,
	@NotEmpty String name,
	@NotEmpty String phoneNumber,
	@NotEmpty String password,
	@NotEmpty String address){
	public ConsumerCreateReqestDto {
		Validator.validateEmail(email);
		Validator.validatePhoneNumber(phoneNumber);
	}

	public Consumer toEntity(){
		return Consumer.of(this);
	}
}
