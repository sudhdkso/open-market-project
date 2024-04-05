package com.project.openmarket.domain.order.dto.request;

public record OrderRequestDto(Long productId, String status, Long cache, Long point, int count) {

}
