package com.project.openmarket.service.order;

import static com.project.openmarket.domain.order.entity.eums.OrderStatus.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.service.OrderService;
import com.project.openmarket.domain.order.service.SellerOrderService;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.service.ServiceTestMock;

class SellerOrderServiceTest extends ServiceTestMock {
	@InjectMocks
	SellerOrderService sellerOrderService;
	@Mock
	OrderService orderService;
	@Mock
	ProductService productService;
	@Mock
	ConsumerService consumerService;

	@Test
	@DisplayName("order가 유효한 값이면 orderStatus를 변경할 수 있다.")
	void updateOrderStatusTest() {
		Long orderId = 1L;
		given(orderService.getOrderById(anyLong())).willReturn(order);

		assertThatNoException()
			.isThrownBy(() -> sellerOrderService.updateOrderStatus(orderId, DELIVERY_START.getStatus()));

		then(order)
			.should(times(1))
			.updateOrderStatus(DELIVERY_START);

		then(orderRepository)
			.should(times(1))
			.save(any(Order.class));
	}

	@Test
	@DisplayName("order, product, consumer가 모두 유효하면 주문을 취소할 수 있다.")
	void cancelOrderTest() {
		Long orderId = 1L;
		given(orderService.getOrderById(anyLong())).willReturn(order);

		Long productId = 213L;
		given(order.getProduct()).willReturn(product);
		given(product.getId()).willReturn(productId);
		given(productService.getProductById(anyLong())).willReturn(product);

		Long consumerId = 16L;
		given(order.getConsumer()).willReturn(consumer);
		given(consumer.getId()).willReturn(consumerId);
		given(consumerService.getConsumerById(anyLong())).willReturn(consumer);

		assertThatNoException()
			.isThrownBy(() -> sellerOrderService.cancelOrder(orderId));

		then(orderService)
			.should(times(1))
			.processOrderCancel(any(Order.class), any(Product.class), any(Consumer.class));
	}
}
