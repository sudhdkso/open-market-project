package com.project.openmarket.domain.user.dto.request;

import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.global.validator.Validator;

import jakarta.validation.constraints.NotEmpty;

public record ConsumerCreateReqestDto(
	@NotEmpty String email,
	String name,
	String phoneNumber,
	@NotEmpty String password,
	@NotEmpty String address){
	public ConsumerCreateReqestDto {
		Validator.validateEmail(email);
	}

	public Consumer toEntity(){
		return Consumer.of(this);
	}
}
