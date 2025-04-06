package com.project.openmarket.domain.review.dto.response;

import com.project.openmarket.domain.review.entity.Review;

public record ReviewResponseDto(int rating, String content) {
	public static ReviewResponseDto of(Review review) {
		return new ReviewResponseDto(review.getRating(), review.getContent());
	}
}
