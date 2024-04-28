package com.project.openmarket.service.order;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.project.openmarket.domain.order.service.OrderService;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.domain.user.service.SellerService;
import com.project.openmarket.service.ServiceTestMock;

class OrderServiceTests extends ServiceTestMock {

	@InjectMocks
	private OrderService orderService;
	@Mock
	private SellerService sellerService;
	@Mock
	private ConsumerService consumerService;

	@Test
	@DisplayName("주문 확정 시 고객의 포인트와 판매자의 캐시가 증가한다.")
	void consumerGetPoint(){
		Long amount = 10000L;
		given(order.totalAmount()).willReturn(amount);

		assertThatNoException()
			.isThrownBy(() -> orderService.processConfirmedOrder(order, seller, consumer));


		verify(consumerService,times(1)).processPoints(amount,consumer);
		verify(sellerService,times(1)).processPayment(amount, seller);
	}

}
