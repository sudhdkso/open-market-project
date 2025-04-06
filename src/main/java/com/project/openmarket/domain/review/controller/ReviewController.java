package com.project.openmarket.domain.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.review.dto.request.ReviewCreateRequestDto;
import com.project.openmarket.domain.review.dto.response.ReviewResponseDto;
import com.project.openmarket.domain.review.service.ReviewService;
import com.project.openmarket.global.context.ConsumerThreadLocal;
import com.project.openmarket.global.context.OrderThreadLocal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping("/consumer/{orderId}/review")
	public ResponseEntity<ReviewResponseDto> create(@PathVariable("orderId") String orderId,
		@RequestBody ReviewCreateRequestDto requestDto) {
		ReviewResponseDto responseDto = reviewService.create(OrderThreadLocal.get(), requestDto,
			ConsumerThreadLocal.get());
		return ResponseEntity.ok(responseDto);
	}
}
