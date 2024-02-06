package com.project.openmarket.domain.user.dto.request;

import com.project.openmarket.domain.user.entity.Seller;

public record SellerCreateRequestDto(String email, String name, String phoneNumber, String password){
	public Seller toEntity(){
		return Seller.of(this);
	}
}
