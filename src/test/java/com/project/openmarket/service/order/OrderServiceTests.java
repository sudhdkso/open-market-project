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
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.domain.user.service.SellerService;
import com.project.openmarket.global.exception.CustomException;
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

	@DisplayName("주문 확정 시 고객 포인트와 판매자 캐시가 적립된다.")
	@Test
	void whenConfirmOrder_thenAddPointAndCash() {
		Long amount = 10000L;
		given(order.getTotalAmount()).willReturn(amount);

		Long expectedPoint = Calculator.getPoint(amount);
		Long expectedRevenue = Calculator.getRevenue(amount);

		assertThatNoException()
			.isThrownBy(() -> orderService.processConfirmedOrder(order, seller, consumer));

		verify(consumerService, times(1)).processPoints(anyLong(), any(Consumer.class));
		verify(sellerService, times(1)).processPayment(anyLong(), any(Seller.class));
	}

	@Nested
	@DisplayName("주문 조회 시 ")
	class findOrder {
		@DisplayName("유효한 주문 id를 가지고 주문을 조회하면 성공한다.")
		@Test
		void whenValidOrderId_thenReturnOrder() {
			given(orderRepository.findById(any())).willReturn(Optional.of(order));
			assertThatNoException()
				.isThrownBy(() -> orderService.getOrderById("aaa"));

		}

		@DisplayName("유효하지 않은 주문 id를 가지고 주문을 조회하면 오류가 발생한다..")
		@Test
		void whenInvalidOrderId_thenThrowException() {
			given(orderRepository.findById(any())).willReturn(Optional.empty());

			assertThatThrownBy(() -> orderService.getOrderById("aaa"))
				.isInstanceOf(CustomException.class)
				.hasMessage(NOT_FOUND_ORDER.getMessage());

		}
	}

	@DisplayName("주문 취소 시 재고와 소지금이 함께 복구된다.")
	@Test
	void whenCancelOrder_thenRestockProductAndRefundConsumer() {
		int count = 1;
		Amount amount = new Amount(1000L, 0L);
		given(order.getCount()).willReturn(count);
		given(order.getAmount()).willReturn(amount);

		assertThatNoException()
			.isThrownBy(() -> orderService.processOrderCancel(order, product, consumer));

		verify(productService, times(1)).increaseProductStock(anyInt(), any(Product.class));
		verify(consumerService, times(1)).increaseAmount(any(Amount.class), any(Consumer.class));
		verify(order, times(1)).updateOrderStatus(any(OrderStatus.class));
	}

	@DisplayName("주문을 리뷰 처리하면 isReviewed 필드가 true로 업데이트된다.")
	@Test
	void markAsReviewed_updatesFlag() {
		//given
		given(order.getId()).willReturn("67efeee0a7d6d52024e6ac6a");
		//when
		orderService.markAsReviewed(order);
		//then
		verify(orderRepository, times(1)).updateIsReviewed(anyString());
	}
}
