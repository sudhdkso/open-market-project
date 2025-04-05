package com.project.openmarket.domain.order.dto.response;

import java.util.List;

import com.project.openmarket.domain.order.entity.Order;

public record OrderListResponsesDto(List<OrderListResponseDto> orders) {
	public static OrderListResponsesDto of(List<Order> order) {
		return new OrderListResponsesDto(
			order.stream()
				.map(OrderListResponseDto::of)
				.toList()
		);
	}
}
