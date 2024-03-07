package com.project.openmarket.domain.user.dto.request;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.validator.Validator;

import jakarta.validation.constraints.NotEmpty;

public record ConsumerCreateReqestDto(
	@NotEmpty String email,
	String name,
	String phoneNumber,
	@NotEmpty String password,
	@NotEmpty String address){
	public ConsumerCreateReqestDto {
		if(!Validator.validateEmail(email)){
			throw new CustomException(INVALID_DATA_INPUT);
		}
		if(phoneNumber!= null && !Validator.validatePhoneNumber(phoneNumber)){
			throw new CustomException(INVALID_DATA_INPUT);
		}
	}

	public Consumer toEntity(){
		return Consumer.of(this);
	}
}
