package com.project.openmarket.domain.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
	public void create(ReviewCreateResponseDto response, Consumer consumer){
		Review.of(response, consumer);
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
	public long getAvgScore(Product product){
		return reviewRepository.getAvgScoreByProduct(product);
	}


}
