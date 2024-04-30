package com.project.openmarket.service.order;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;
import com.project.openmarket.domain.order.service.OrderService;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.domain.user.service.SellerService;
import com.project.openmarket.global.util.Calculator;
import com.project.openmarket.service.ServiceTestMock;

class OrderServiceTests extends ServiceTestMock {

	@InjectMocks
	private OrderService orderService;
	@Mock
	private SellerService sellerService;
	@Mock
	private ConsumerService consumerService;
	@Mock
	private ProductService productService;

	@Test
	@DisplayName("주문 확정 시 고객의 포인트와 판매자의 캐시가 증가한다.")
	void consumerGetPoint(){
		Long amount = 10000L;
		given(order.getTotalAmount()).willReturn(amount);

		Long expectedPoint = Calculator.getPoint(amount);
		Long expectedRevenue = Calculator.getRevenue(amount);

		assertThatNoException()
			.isThrownBy(() -> orderService.processConfirmedOrder(order, seller, consumer));

		then(consumerService)
			.should(times(1))
			.processPoints(expectedPoint, consumer);

		then(sellerService)
			.should(times(1))
			.processPayment(expectedRevenue, seller);
	}

	@Nested
	@DisplayName("주문 조회 시 ")
	class findOrder{
		@Test
		@DisplayName("유효한 주문 id를 가지고 주문을 조회하면 성공한다.")
		void findByValidOrderId(){
			given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
			assertThatNoException()
				.isThrownBy(() -> orderService.getOrderById(1L));

		}

		@Test
		@DisplayName("유효하지 않은 주문 id를 가지고 주문을 조회하면 오류가 발생한다..")
		void findByInvalidOrderId(){
			given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

			assertThatThrownBy(() -> orderService.getOrderById(1L))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_FOUND_ORDER.getMessage());


		}
	}

	@Test
	@DisplayName("구매 취소 메서드를 호출하면 상품의 재고를 증가시키는 메서드와 고객의 소지금을 증가시키는 메서드가 함께 호출된다.")
	void processOrderCancelTest() {
		int count = 1;
		Amount amount = new Amount(1000L,0L);
		given(order.getCount()).willReturn(count);
		given(order.getAmount()).willReturn(amount);

		assertThatNoException()
			.isThrownBy(() -> orderService.processOrderCancel(order, product, consumer));

		then(productService)
			.should(times(1))
			.increaseProductStock(count, product);

		then(consumerService)
			.should(times(1))
			.increaseAmount(amount, consumer);

		then(order)
			.should(times(1))
			.updateOrderStatus(any(OrderStatus.class));

	}
}
