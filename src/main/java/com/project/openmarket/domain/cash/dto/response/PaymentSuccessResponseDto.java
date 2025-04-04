package com.project.openmarket.domain.cash.dto.response;

import com.project.openmarket.domain.cash.entity.CashHistory;

public record PaymentSuccessResponseDto(String orderId, String paymentKey, Long amaount) {
	public static PaymentSuccessResponseDto of(CashHistory cashHistory) {
		return new PaymentSuccessResponseDto(cashHistory.getOrderId(), cashHistory.getPaymentKey(),
			cashHistory.getAmount());
	}

}
