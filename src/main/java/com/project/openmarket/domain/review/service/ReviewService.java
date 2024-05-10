package com.project.openmarket.domain.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.review.dto.request.ReviewCreateResponseDto;
import com.project.openmarket.domain.review.entity.Review;
import com.project.openmarket.domain.review.repository.ReviewRepository;
import com.project.openmarket.domain.user.entity.Consumer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final ProductService productService;

	//리뷰 생성
	//평균 리뷰 테이블 업데이트
	@Transactional
	public Review create(ReviewCreateResponseDto response, Consumer consumer){
		//1. 리뷰 생성
		Product product = response.order().getProduct();
		Review review = reviewRepository.save(Review.of(response, consumer));

		//2. 평균 리뷰 평점 테이블 업데이트
		double avgScore = getAvgScore(product);
		productService.updateProductAvgScore(avgScore, product);
		return review;
	}

	//productId에 따른 상품 리뷰 모두 조회
	public List<Review> getReviewByProductId(Long productId){
		Product product = productService.getProductById(productId);
		return reviewRepository.findByProduct(product);
	}

	//고객 별 리뷰 모두 조회
	public List<Review> getReviewByConsumer(Consumer consumer){
		return reviewRepository.findByConsumer(consumer);
	}

	//리뷰 평균 점수
	public double getAvgScore(Product product){
		return reviewRepository.getAvgScoreByProduct(product);
	}
}
