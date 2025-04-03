package com.project.openmarket.domain.cache.dto.request;

import com.project.openmarket.domain.cache.entity.CashHistory;
import com.project.openmarket.domain.cache.entity.enums.CashStatus;
import com.project.openmarket.domain.user.entity.Consumer;

public record PaymentRequestDto(String orderId, String amount) {
	public CashHistory toEntity(Consumer consumer){
		return new CashHistory(consumer, Long.parseLong(amount), orderId, null, CashStatus.PENDING);
	}
}
