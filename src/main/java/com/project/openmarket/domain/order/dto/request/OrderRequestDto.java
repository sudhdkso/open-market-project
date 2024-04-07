package com.project.openmarket.domain.order.dto.request;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.validator.NumberValidator;

public record OrderRequestDto(Long productId, String status, Long cache, Long point, int count) {
	public OrderRequestDto{
		if(!NumberValidator.isPositive(count)){
			throw new CustomException(NOT_POSITIVE_NUMBER);
		}
	}
}
