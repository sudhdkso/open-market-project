package com.project.openmarket.global.exception.enums;

import lombok.Getter;

@Getter
public enum ExceptionConstants {


	// Custom Exception
	SECURITY("CE0001", "로그인이 필요합니다"),
	ALREADY_EXISTS_EMAIL("CE0002","존재하는 이메일입니다"),
	NOT_FOUND_USER("CE0003", "사용자를 찾을 수 없습니다"),
	NOT_MATCH_PASSWORD("CE0004","비밀번호가 일치하지 않습니다"),
	INVALID_DATA_INPUT("CE0005","잘못된 입력입니다"),
	ALREADY_EXISTS_PRODUCT("CE0006","판매자에게 중복된 상품이 존재합니다"),

	NOT_FOUND_PRODUCT("CE0007","상품을 찾을 수 없습니다"),
	NOT_FOUND_ORDER("CE0008", "주문을 찾을 수 없습니다"),
	NOT_ENOUGH_STOCK("CE0009","재고가 부족합니다"),
	NOT_ENOUTH_CACHE("CE0010","소지금이 부족합니다"),
	NOT_POSITIVE_NUMBER("CE0011","숫자가 양수가 아닙니다.");

	private final String code;
	private String message;

	ExceptionConstants(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
