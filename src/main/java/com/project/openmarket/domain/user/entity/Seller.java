package com.project.openmarket.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document(collection = "seller")
public class Seller extends User{

	private Seller(String email, String name, String phoneNumber, String passowrd){
		super(email, name, phoneNumber, passowrd);
	}

	public static Seller of(SellerCreateRequestDto dto){
		return new Seller(dto.email(), dto.name(), dto.phoneNumber(), dto.password());
	}
}
