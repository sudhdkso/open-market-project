package com.project.openmarket.domain.cash.entity.enums;

import lombok.Getter;

@Getter
public enum PayType {
	CARD("카드"), // 카드
	TRANSFER("계좌 이체"), //계좌이체,
	VIRTUAL_ACCOUNT("가상 계좌"),  //가상 계좌
	MOBILE_PHONE("휴대폰 결제"), //휴대폰 결제
	CULTURE_GIFT_CERTIFICATE("문화 상품권"), //문화 상품권 결제
	FOREIGN_EASY_PAY("해외 간편 결제"); //해외 간편 결제
	private String value;

	PayType(String value) {
		this.value = value;
	}
}
