package com.project.openmarket.domain.cash.entity.enums;

import lombok.Getter;

@Getter
public enum CashStatus {
	PENDING("결제 대기"),   // 결제 대기 (아직 결제 승인되지 않음)
	COMPLETED("결제 완료"), // 결제 완료 (결제 승인됨)
	CANCELED("결제 취소");   // 결제 취소됨

	private String value;

	CashStatus(String value) {
		this.value = value;
	}

}
