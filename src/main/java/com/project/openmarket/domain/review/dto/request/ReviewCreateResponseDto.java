package com.project.openmarket.domain.review.dto.request;

import com.project.openmarket.domain.order.entity.Order;

public record ReviewCreateResponseDto(Order order, int score) {
}
