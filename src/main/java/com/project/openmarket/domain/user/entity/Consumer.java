package com.project.openmarket.domain.user.entity;

import java.util.Objects;

import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "consumers")
public class Consumer extends User{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, updatable = false, columnDefinition = "BIGINT")
	private Long id;

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

	public Long getPoint() {
		return point;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Consumer consumer = (Consumer)o;
		return Objects.equals(id, consumer.id) && Objects.equals(address, consumer.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, address);
	}
}