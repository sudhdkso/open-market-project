package com.project.openmarket.domain.order.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.repository.OrderRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PurchaseConfirmationService {
	private final OrderRepository orderRepository;
	private final OrderService orderService;

	// 배송 완료 후 일정 시간이 지난 주문을 자동으로 구매 확정 처리
	@Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
	public void autoConfirmPurchase() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime cutoffTime = now.minusDays(1);
		List<Order> orders = orderRepository.findOrdersWithDeliveryCompleteTimeExceedingThreshold(cutoffTime);

		for (Order order : orders) {
			orderService.processConfirmedOrder(order, order.getSeller(), order.getConsumer());
		}
	}
}
