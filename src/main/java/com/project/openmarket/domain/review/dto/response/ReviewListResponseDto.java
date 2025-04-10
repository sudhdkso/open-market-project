package com.project.openmarket.domain.review.dto.response;

import java.time.LocalDateTime;

import com.project.openmarket.domain.review.entity.Review;
import com.project.openmarket.domain.user.dto.reposne.UserSimpleResponseDto;

public record ReviewListResponseDto(String id, String content, int rating, LocalDateTime createdAt,
									UserSimpleResponseDto reviewer) {
	public static ReviewListResponseDto of(Review review) {
		return new ReviewListResponseDto(review.getId(), review.getContent(), review.getRating(), review.getCreatedAt(),
			UserSimpleResponseDto.of(review.getConsumer()));
	}
}
