package com.project.openmarket.service.order;

import static com.project.openmarket.domain.order.entity.eums.OrderStatus.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.project.openmarket.domain.order.dto.response.OrderResponseDto;
import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.service.OrderService;
import com.project.openmarket.domain.order.service.SellerOrderService;
import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
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


	@Test
	@DisplayName("판매자 주문이 있는 경우 findOrdersBySellerId는 orderList를 return한다.")

	void findOrdersBySeller_success() {
		// given

		var sellerReuestDto = new SellerCreateRequestDto("seller@example.com","seller","010-0000-0000","1234");
		var seller = spy(Seller.of(sellerReuestDto));

		var productRequestDto = new ProductRequestDto("product",1000,2);
		var product = Product.of(productRequestDto, seller);
		var order1 = new Order(product, null, new Amount(1000L, 0L), 0, null);
		var order2 = new Order(product, null, new Amount(0L,1000L), 0, null);

		List<Order> mockOrders = Arrays.asList(order1, order2);

		given(seller.getId()).willReturn(1L);
		when(orderRepository.findOrdersBySellerId(any())).thenReturn(mockOrders);

		// when
		List<OrderResponseDto> result = sellerOrderService.findOrdersBySeller(seller);

		// then
		assertEquals(2, result.size());
		assertEquals(order1.getSeller(), result.get(0).order().getSeller());
		assertEquals(order2.getSeller(), result.get(0).order().getSeller());

		verify(orderRepository, times(1)).findOrdersBySellerId(seller.getId());
	}


	@Test
	@DisplayName("판매자 주문이 없는 경우 findOrdersBySellerId는 빈 리스트를 return한다.")
	void findOrdersBySeller_noOrdersFound() {

		given(seller.getId()).willReturn(1L);
		when(orderRepository.findOrdersBySellerId(any())).thenReturn(List.of());

		List<OrderResponseDto> result = sellerOrderService.findOrdersBySeller(seller);

		assertTrue(result.isEmpty());
		verify(orderRepository, times(1)).findOrdersBySellerId(seller.getId());
	}
}
