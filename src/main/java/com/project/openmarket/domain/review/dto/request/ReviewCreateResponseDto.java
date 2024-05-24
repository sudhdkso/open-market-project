package com.project.openmarket.domain.review.dto.request;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.exception.enums.ExceptionConstants;
import com.project.openmarket.global.validator.NumberValidator;

public record ReviewCreateResponseDto(Order order, int score) {
	public ReviewCreateResponseDto {
		if(!NumberValidator.isScoreWithinRange(score)){
			throw new CustomException(ExceptionConstants.SCORE_OUT_OF_RANGE);
		}
	}
}
