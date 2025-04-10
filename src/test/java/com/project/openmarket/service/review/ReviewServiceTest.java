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
import org.springframework.context.ApplicationEventPublisher;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.service.OrderService;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.review.dto.request.ReviewCreateRequestDto;
import com.project.openmarket.domain.review.entity.Review;
import com.project.openmarket.domain.review.service.ReviewService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.service.ServiceTestMock;

class ReviewServiceTest extends ServiceTestMock {
	@InjectMocks
	ReviewService reviewService;
	@Mock
	ProductService productService;
	@Mock
	OrderService orderService;
	@Mock
	ApplicationEventPublisher applicationEventPublisher;

	@DisplayName("리뷰를 등록할 때 점수가 유효한 범위내의 값이면 성공한다.")
	@Test
	void whenValidScore_thenCreateReview() {
		//given
		var request = createReview(4);

		given(order.getProduct()).willReturn(product);
		doNothing().when(orderService).markAsReviewed(any(Order.class));
		doNothing().when(applicationEventPublisher).publishEvent(any());
		given(reviewRepository.save(any(Review.class))).willReturn(request.toEntity(product, consumer));

		//when
		assertThatNoException()
			.isThrownBy(() -> reviewService.create(order, request, consumer));

		//then
		verify(reviewRepository, times(1)).save(any(Review.class));
	}

	@DisplayName("범위 밖의 점수로 리뷰 등록 시 실패한다.")
	@ParameterizedTest
	@ValueSource(ints = {-1, 0, 6, 10})
	void whenInvalidScore_thenFailToCreateReview(int score) {
		assertThatThrownBy(() -> createReview(score))
			.isInstanceOf(CustomException.class)
			.hasMessage(SCORE_OUT_OF_RANGE.getMessage());
	}

	@DisplayName("유효한 상품의 리뷰를 조회한다.")
	@Test
	void whenValidProduct_thenReturnReviewList() {
		given(productService.getProductById(any())).willReturn(product);

		assertThatNoException()
			.isThrownBy(() -> reviewService.getReviewByProductId("67ea40df5aeb2844f05b84e8"));

		then(reviewRepository)
			.should(times(1))
			.findByProductId(any(Product.class));
	}

	@DisplayName("유효한 고객의 리뷰를 조회한다.")
	@Test
	void whenValidConsumer_thenReturnReviewList() {
		assertThatNoException()
			.isThrownBy(() -> reviewService.getReviewByConsumer(consumer));

		then(reviewRepository)
			.should(times(1))
			.findByConsumer(any(Consumer.class));
	}

	ReviewCreateRequestDto createReview(int score) {
		return new ReviewCreateRequestDto(score, "review");
	}
}
