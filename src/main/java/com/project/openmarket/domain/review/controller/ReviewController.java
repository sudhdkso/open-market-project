package com.project.openmarket.domain.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.review.dto.request.ReviewCreateRequestDto;
import com.project.openmarket.domain.review.dto.response.ReviewListResponsesDto;
import com.project.openmarket.domain.review.dto.response.ReviewResponseDto;
import com.project.openmarket.domain.review.service.ReviewService;
import com.project.openmarket.global.context.ConsumerThreadLocal;
import com.project.openmarket.global.context.OrderThreadLocal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping("/consumer/orders/{orderId}/review")
	public ResponseEntity<ReviewResponseDto> create(@PathVariable("orderId") String orderId,
		@RequestBody ReviewCreateRequestDto requestDto) {
		ReviewResponseDto responseDto = reviewService.create(OrderThreadLocal.get(), requestDto,
			ConsumerThreadLocal.get());
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/products/{productId}/reviews")
	public ResponseEntity<ReviewListResponsesDto> getAllReviewByProductId(@PathVariable("productId") String productId) {
		ReviewListResponsesDto responsesDto = reviewService.getReviewByProductId(productId);
		return ResponseEntity.ok(responsesDto);
	}

	@GetMapping("/consumer/reviews")
	public ResponseEntity<?> getConsumerReviews() {
		ReviewListResponsesDto responsesDto = reviewService.getReviewByConsumer(ConsumerThreadLocal.get());
		return ResponseEntity.ok(responsesDto);
	}
}
