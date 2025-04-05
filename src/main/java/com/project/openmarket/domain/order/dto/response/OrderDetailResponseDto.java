package com.project.openmarket.domain.order.dto.response;

import java.time.LocalDateTime;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;
import com.project.openmarket.domain.user.entity.Consumer;

public record OrderDetailResponseDto(String id, String productName, Long totalAmount, Long cash, Long point,
									 String address, Consumer consumer, OrderStatus status, LocalDateTime createdAt) {
	public static OrderDetailResponseDto of(Order order) {
		return new OrderDetailResponseDto(order.getId(), order.getProduct().getName(), order.getTotalAmount(),
			order.getAmount().getCash(), order.getAmount().getPoint(), order.getDeliveryAddress(), order.getConsumer(),
			order.getStatus(), order.getCreatedAt());
	}
}
