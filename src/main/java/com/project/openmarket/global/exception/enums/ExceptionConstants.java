package com.project.openmarket.global.exception.enums;

import lombok.Getter;

@Getter
public enum ExceptionConstants {


	// Custom Exception
	SECURITY("CE0100", "로그인이 필요합니다."),
	ALREADY_EXISTS_EMAIL("CE0101","존재하는 이메일입니다."),
	NOT_FOUND_USER("CE0102", "사용자를 찾을 수 없습니다."),
	NOT_MATCH_PASSWORD("CE0103","비밀번호가 일치하지 않습니다."),
	INVALID_DATA_INPUT("CE0104","잘못된 입력입니다."),

	ALREADY_EXISTS_PRODUCT("CE0200","판매자에게 중복된 상품이 존재합니다."),
	NOT_FOUND_PRODUCT("CE0201","상품을 찾을 수 없습니다."),

	NOT_FOUND_ORDER("CE0300", "주문을 찾을 수 없습니다."),
	NOT_ENOUGH_STOCK("CE0301","재고가 부족합니다."),
	NOT_ENOUTH_CACHE("CE0302","소지금이 부족합니다."),
	CANNOT_CANCLED_ORDER("CE0303","주문을 취소할 수 없습니다."),
	CANNOT_CONFIRM_ORDER("CE0304","주문을 확정할 수 없습니다."),
	NOT_MATCH_PRICE("CE0305", "상품가격과 주문 가격이 일치하지 않습니다."),

	NOT_POSITIVE_NUMBER("CE0001","숫자가 양수가 아닙니다."),
	SCORE_OUT_OF_RANGE("CE002","스코어가 1~5사이의 숫자가 아닙니다.");
	private final String code;
	private String message;

	ExceptionConstants(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
