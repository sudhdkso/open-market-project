package com.project.openmarket.domain.user.entity;

import java.util.List;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "sellers")
public class Seller extends User{

	@OneToMany(mappedBy = "seller")
	private List<Product> products;

	private Seller(String email, String name, String phoneNumber, String passowrd){
		super(email, name, phoneNumber, passowrd);
	}

	public static Seller of(SellerCreateRequestDto dto){
		return new Seller(dto.email(), dto.name(), dto.phoneNumber(), dto.password());
	}
}
