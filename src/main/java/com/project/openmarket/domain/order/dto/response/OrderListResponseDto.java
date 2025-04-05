package com.project.openmarket.domain.order.dto.response;

import java.time.LocalDateTime;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;

public record OrderListResponseDto(String id, String productName, Long totalAmount, LocalDateTime createdAt,
								   OrderStatus status) {
	public static OrderListResponseDto of(Order order) {
		return new OrderListResponseDto(order.getId(), order.getProduct().getName(), order.getTotalAmount(),
			order.getCreatedAt(), order.getStatus());
	}
}
