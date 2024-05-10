package com.project.openmarket.service.review;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.review.dto.request.ReviewCreateResponseDto;
import com.project.openmarket.domain.review.entity.Review;
import com.project.openmarket.domain.review.service.ReviewService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.service.ServiceTestMock;

class ReviewServiceTest extends ServiceTestMock {
	@InjectMocks
	ReviewService reviewService;
	@Mock
	ProductService productService;

	@Test
	@DisplayName("리뷰를 등록할 때 점수가 유효한 범위내의 값이면 성공한다.")
	void createdReviewWithInScore() {
		var response = createReview(4);

		given(order.getProduct()).willReturn(product);

		assertThatNoException()
			.isThrownBy(() -> reviewService.create(response, consumer));

		then(reviewRepository)
			.should(times(1))
			.save(any(Review.class));

		then(productService)
			.should(times(1))
			.updateProductAvgScore(anyDouble(),any(Product.class));
	}

	@DisplayName("범위 밖의 점수를 가지고 리뷰를 등록하려하면 실패한다.")
	@ParameterizedTest
	@ValueSource(ints = {-1,0,6,10})
	void createReviewNotWithInScore(int score) {
		assertThatThrownBy(() -> createReview(score))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(SCORE_OUT_OF_RANGE.getMessage());
	}

	@Test
	@DisplayName("유효한 상품으로 리뷰를 조회할 수 있다.")
	void getValidReviewListByProductTest(){
		Long proudctId = 1000L;
		given(productService.getProductById(anyLong())).willReturn(product);

		assertThatNoException()
			.isThrownBy(() -> reviewService.getReviewByProductId(proudctId));

		then(reviewRepository)
			.should(times(1))
			.findByProduct(any(Product.class));
	}

	@Test
	@DisplayName("유효한 고객으로 리뷰를 조회할 수 있다.")
	void getValidReviewListByConsumer() {
		assertThatNoException()
			.isThrownBy(() -> reviewService.getReviewByConsumer(consumer));

		then(reviewRepository)
			.should(times(1))
			.findByConsumer(any(Consumer.class));
	}

	@Test
	@DisplayName("product의 평균 리뷰 점수를 확인할 수 있다.")
	void getAvgScoreTest() {
		double expectedAvg = 4.5;

		when(reviewRepository.getAvgScoreByProduct(product)).thenReturn(expectedAvg);

		double realAvg = reviewService.getAvgScore(product);

		assertThat(realAvg).isCloseTo(expectedAvg, offset(0.001d));

		then(reviewRepository)
			.should(times(1))
			.getAvgScoreByProduct(any(Product.class));
	}

	ReviewCreateResponseDto createReview(int score){
		return new ReviewCreateResponseDto(order, score);
	}
}
