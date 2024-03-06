package com.project.openmarket.domain.user.dto.request;

import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.global.validator.Validator;

import jakarta.validation.constraints.NotEmpty;

public record SellerCreateRequestDto(
	@NotEmpty String email,
	String name,
	String phoneNumber,
	@NotEmpty String password){
	public SellerCreateRequestDto{
		if(!Validator.validateEmail(email)){
			throw new IllegalArgumentException();
		}
	}
	public Seller toEntity(){
		return Seller.of(this);
	}
}
