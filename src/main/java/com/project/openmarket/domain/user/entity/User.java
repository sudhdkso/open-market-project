package com.project.openmarket.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class User {

	@Id
	@Column(name = "id", unique = true, nullable = false, updatable = false, columnDefinition = "BIGINT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", updatable = false, nullable = false)
	private String email;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "cache", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
	private Long cache;

	@Column(name = "phone_number", updatable = false, nullable = false)
	private String phoneNumber;

	@Column(name = "password", updatable = false, nullable = false)
	private String password;

	public User(String email, String name, String phoneNumber, String password) {
		this.email = email;
		this.name = name;
		this.cache = 0L;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}
}
