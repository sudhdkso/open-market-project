package com.project.openmarket.domain.product.listener;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.repository.ProductRepository;
import com.project.openmarket.domain.review.event.ReviewCreatedEvent;
import com.project.openmarket.domain.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewEventListener {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ProductRepository productRepository;
	private final ReviewRepository reviewRepository;

	@EventListener
	public void handleReviewCreatedEvent(ReviewCreatedEvent event) {
		logger.info("productId = {} 이벤트 발생!", event.getProductId());

		ObjectId productId = event.getProductId();

		// 상품의 평균 평점 계산
		double avgRating = reviewRepository.calculateAvgScoreByProductId(productId);
		logger.info("현재 상품 평점 = {}", avgRating);
		// 해당 상품의 평균 평점 업데이트
		Product product = productRepository.getById(productId);
		product.updateAvgRating(avgRating);
		productRepository.save(product);  // 상품 정보 갱신
	}

}
