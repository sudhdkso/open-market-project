package com.project.openmarket.service.order;

import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.service.OrderService;
import com.project.openmarket.domain.order.service.PurchaseConfirmationService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.service.ServiceTestMock;

class PurchaseConfirmationServiceTests extends ServiceTestMock {
	@InjectMocks
	PurchaseConfirmationService purchaseConfirmationService;
	@Mock
	OrderService orderService;


	@Test
	@DisplayName("배송 완료 후 1일 뒤 자동 구매 확정된다.")
	void testAutoConfirmed(){

		// 테스트 데이터 생성
		LocalDateTime dateTime = LocalDateTime.now().minusDays(1);

		List<Order> list = new ArrayList<>();
		list.add(order);

		given(orderRepository.findOrdersWithDeliveryCompleteTimeExceedingThreshold(any(LocalDateTime.class)))
			.willReturn(list);

		given(order.getSeller()).willReturn(seller);
		given(order.getConsumer()).willReturn(consumer);


		// 주문이 24시간 이전에 배송 완료되었을 때 구매 확정되는지 확인
		purchaseConfirmationService.autoConfirmPurchase();
		// 주문이 자동으로 구매 확정되는지 확인
		then(orderService)
			.should(times(1))
			.processConfirmedOrder(any(Order.class), any(Seller.class), any(Consumer.class));

	}

}
