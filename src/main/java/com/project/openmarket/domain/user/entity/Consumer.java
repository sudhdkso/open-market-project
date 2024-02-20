package com.project.openmarket.domain.user.entity;

import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "consumers")
public class Consumer extends User {
	@Column(name = "address")
	private String address;

	@Column(name = "point")
	private Long point;

	public Consumer(String email, String name, String phoneNumber, String address, String password){
		super(email,name, phoneNumber, password);
		this.address = address;
		this.point = 0L;
	}

	public static Consumer of(ConsumerCreateReqestDto dto){
		return new Consumer(dto.email(), dto.name(), dto.phoneNumber(), dto.address(), dto.password());
	}

	public String getAddress() {
		return address;
	}

	public Long getPoint() {
		return point;
	}
}
