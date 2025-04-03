package com.project.openmarket.domain.cache.dto.request;

import java.util.HashMap;
import java.util.Map;

public record PaymentConfirmRequestDto(String orderId, String paymentKey, String amount) {

	public Map<String, String> toMap(){
		Map<String, String> map = new HashMap<>();
		map.put("orderId", orderId);
		map.put("paymentKey", paymentKey);
		map.put("amount", amount);

		return map;
	}
}
