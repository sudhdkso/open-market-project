package com.project.openmarket.domain.review.dto.response;

import java.util.List;

import com.project.openmarket.domain.review.entity.Review;

public record ReviewListResponsesDto(List<ReviewListResponseDto> reviews) {
	public static ReviewListResponsesDto of(List<Review> reviews) {
		return new ReviewListResponsesDto(
			reviews.stream()
				.map(ReviewListResponseDto::of)
				.toList()
		);
	}
}
