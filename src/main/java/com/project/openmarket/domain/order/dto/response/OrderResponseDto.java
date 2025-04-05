package com.project.openmarket.domain.order.dto.response;

import java.time.LocalDateTime;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.product.dto.response.ProductResponseDto;

public record OrderResponseDto(String id, ProductResponseDto product, Long amount, Long point, Long totalAmount,
							   LocalDateTime createdAt) {

	public static OrderResponseDto of(Order order) {
		return new OrderResponseDto(order.getId(), ProductResponseDto.of(order.getProduct()),
			order.getAmount().getCash(),
			order.getAmount().getPoint(), order.getTotalAmount(), order.getCreatedAt());
	}

}
