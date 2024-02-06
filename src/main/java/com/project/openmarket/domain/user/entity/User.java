package com.project.openmarket.domain.user.entity;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {

	@Column(name = "email", unique = true, updatable = false, nullable = false)
	private String email;
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "cache", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
	private Long cache;

	@Getter
	@Column(name = "email", unique = true, updatable = false, nullable = false)
	private String phoneNumber;

	@Column(name = "password", updatable = false, nullable = false)
	private String password;
	public User(String email, String name, String phoneNumber, String password){
		this.email = email;
		this.name = name;
		this.cache = 0L;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}

}
