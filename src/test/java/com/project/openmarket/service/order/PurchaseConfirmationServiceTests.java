package com.project.openmarket.service.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.service.OrderService;
import com.project.openmarket.domain.order.service.PurchaseConfirmationService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.domain.user.service.SellerService;
import com.project.openmarket.service.ServiceTestMock;

public class PurchaseConfirmationServiceTests extends ServiceTestMock {
	@InjectMocks
	private PurchaseConfirmationService autoConfirmPurchase;
	@Mock
	private OrderService orderService;
	@Mock
	private ConsumerService consumerService;
	@Mock
	private SellerService sellerService;


	@Test
	@DisplayName("배송 완료 후 1일 뒤 자동 구매 확정된다.")
	void testAutoConfirmed(){

		List<Order> list = new ArrayList<>();
		list.add(order);

		given(orderRepository.findByStatusAndDeliveryCompleteTimeBefore(any(LocalDateTime.class)))
			.willReturn(list);

		given(order.getSellerId()).willReturn(new ObjectId("67ea40df5aeb2844f05b84e8"));
		given(sellerService.findById(any())).willReturn(seller);
		given(order.getConsumerId()).willReturn(new ObjectId("67ec1324da973979b3723d17"));
		given(consumerService.getConsumerById(any())).willReturn(consumer);


		// 주문이 24시간 이전에 배송 완료되었을 때 구매 확정되는지 확인
		autoConfirmPurchase.autoConfirmPurchase();

		// 주문이 자동으로 구매 확정되는지 확인
		then(orderService)
			.should(times(1))
			.processConfirmedOrder(any(Order.class), any(Seller.class), any(Consumer.class));

	}
}
