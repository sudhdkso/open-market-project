package com.project.openmarket.global.auth.enums;

import java.util.Arrays;

import lombok.Getter;

public enum Role {
	EMPTY("empty","없음"),
	SELLER("seller","판매자"),
	CONSUMER("consumer", "고객");
	@Getter
	private final String key;
	private final String value;

	Role(String key, String value){
		this.key = key;
		this.value = value;
	}

	public static Role findRoleByKey(String key){
		return Arrays.stream(values())
			.filter(role ->role.getKey().equals(key))
			.findAny()
			.orElse(EMPTY);
	}

}
