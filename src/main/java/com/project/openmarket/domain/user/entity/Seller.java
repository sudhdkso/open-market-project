package com.project.openmarket.domain.user.entity;

import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sellers")
public class Seller extends User{

	@Id
	@Column(name = "id", unique = true, nullable = false, updatable = false, columnDefinition = "BIGINT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Seller(String email, String name, String phoneNumber, String passowrd){
		super(email, name, phoneNumber, passowrd);
	}

	public static Seller of(SellerCreateRequestDto dto){
		return new Seller(dto.email(), dto.name(), dto.phoneNumber(), dto.password());
	}
}
