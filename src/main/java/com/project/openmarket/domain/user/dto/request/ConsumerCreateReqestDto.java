package com.project.openmarket.domain.user.dto.request;

import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.validator.Validator;

public record ConsumerCreateReqestDto(String email, String name,  String phoneNumber, String password, String address){
	public ConsumerCreateReqestDto {
		Validator.validateEmail(email);
		Validator.validatePhoneNumber(phoneNumber);
	}

	public Consumer toEntity(){
		return Consumer.of(this);
	}
}
