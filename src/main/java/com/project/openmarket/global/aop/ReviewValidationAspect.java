package com.project.openmarket.global.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;
import com.project.openmarket.domain.order.repository.OrderRepository;
import com.project.openmarket.global.context.OrderThreadLocal;
import com.project.openmarket.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Aspect
@Component
public class ReviewValidationAspect {
	private final OrderRepository orderRepository;

	@Before("execution(* com.project.openmarket.domain.review.controller.ReviewController.create(..)) && args(orderId, ..)")
	public void checkReviewDuplication(JoinPoint joinPoint, String orderId) {
		Order order = orderRepository.getById(orderId);

		if (!order.getStatus().equals(OrderStatus.PURCHASE_CONFIRMATION)) {
			throw new CustomException("E4000", "구매확정이 되지 않은 주문입니다.");
		}
		if (order.isReviewed()) {
			throw new CustomException("E4001", "이미 작성된 리뷰가 존재합니다.");
		}

		OrderThreadLocal.set(order);
	}

	@After("execution(* com.project.openmarket.domain.review.controller.ReviewController.create(..))")
	public void clearContext() {
		OrderThreadLocal.clear();
	}
}
