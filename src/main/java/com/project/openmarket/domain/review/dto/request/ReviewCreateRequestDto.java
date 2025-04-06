package com.project.openmarket.domain.review.dto.request;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.review.entity.Review;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.exception.enums.ExceptionConstants;
import com.project.openmarket.global.validator.NumberValidator;

public record ReviewCreateRequestDto(int score, String content) {
	public ReviewCreateRequestDto {
		if (!NumberValidator.isScoreWithinRange(score)) {
			throw new CustomException(ExceptionConstants.SCORE_OUT_OF_RANGE);
		}
	}

	public Review toEntity(Product product, Consumer consumer) {
		return new Review(score, content, consumer, product);
	}
}
