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


	public void increaseCacheBalance(final Long amount){
		increaseCache(calcRevenue(amount));
	}

	//판매자 최종 수익 계산하는 함수
	private Long calcRevenue(Long amount) {
		return amount - (long)Math.ceil((double)amount * 0.05);
	}
}
