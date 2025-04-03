package com.project.openmarket.domain.cache.dto.response;

import com.project.openmarket.domain.cache.entity.CashHistory;

public record PaymentSuccessResponseDto(String orderId, String paymentKey, Long amaount) {
	public static PaymentSuccessResponseDto of(CashHistory cashHistory){
		return new PaymentSuccessResponseDto(cashHistory.getOrderId(), cashHistory.getPaymentKey(), cashHistory.getAmount());
	}

}
