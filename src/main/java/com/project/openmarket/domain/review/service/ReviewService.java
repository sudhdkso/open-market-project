package com.project.openmarket.domain.review.service;

import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.service.OrderService;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.review.dto.request.ReviewCreateRequestDto;
import com.project.openmarket.domain.review.dto.response.ReviewListResponsesDto;
import com.project.openmarket.domain.review.dto.response.ReviewResponseDto;
import com.project.openmarket.domain.review.entity.Review;
import com.project.openmarket.domain.review.event.ReviewCreatedEvent;
import com.project.openmarket.domain.review.repository.ReviewRepository;
import com.project.openmarket.domain.user.entity.Consumer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final ProductService productService;
	private final OrderService orderService;
	private final ApplicationEventPublisher applicationEventPublisher;

	//리뷰 생성
	//평균 리뷰 테이블 업데이트
	@Transactional
	public ReviewResponseDto create(Order order, ReviewCreateRequestDto requestDto, Consumer consumer) {
		//1. 리뷰 생성
		Product product = order.getProduct();
		Review review = reviewRepository.save(requestDto.toEntity(product, consumer));
		orderService.markAsReviewed(order);

		//2. 평균 리뷰 평점 테이블 업데이트
		applicationEventPublisher.publishEvent(new ReviewCreatedEvent(this, product.getId()));
		return ReviewResponseDto.of(review);
	}

	//productId에 따른 상품 리뷰 모두 조회
	public ReviewListResponsesDto getReviewByProductId(String productId) {
		Product product = productService.getProductById(new ObjectId(productId));
		return ReviewListResponsesDto.of(reviewRepository.findByProduct(product));
	}

	//고객 별 리뷰 모두 조회
	public ReviewListResponsesDto getReviewByConsumer(Consumer consumer) {
		return ReviewListResponsesDto.of(reviewRepository.findByConsumer(consumer));
	}

}
