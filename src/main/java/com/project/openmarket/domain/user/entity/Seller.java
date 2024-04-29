package com.project.openmarket.domain.user.entity;

import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "sellers")
public class Seller extends User{

	private Seller(String email, String name, String phoneNumber, String passowrd){
		super(email, name, phoneNumber, passowrd);
	}

	public static Seller of(SellerCreateRequestDto dto){
		return new Seller(dto.email(), dto.name(), dto.phoneNumber(), dto.password());
	}
}
