package com.project.openmarket.entity;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import com.project.openmarket.domain.order.dto.request.OrderRequestDto;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Consumer;

class OrderTest {
	@Mock
	Product product;
	@Mock
	Consumer consumer;

	@ParameterizedTest
	@DisplayName("주문 수량이 양수이면 성공적으로 주문이 생성된다.")
	@ValueSource(ints = {1,5,100})
	void createOrderWithPositiveCount(int count){

		assertThatNoException()
			.isThrownBy(() -> createOrder(count));
	}


	@ParameterizedTest
	@DisplayName("주문 수량이 음수이면 오류가 발생한다.")
	@ValueSource(ints = {-1,-5,-100})
	void createOrderWithNegativeCount(int count){
		assertThatThrownBy(() -> createOrder(count))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(NOT_POSITIVE_NUMBER.getMessage());
	}

	@Test
	@DisplayName("주문 상태를 메서드를 통해 업데이트할 수 있다.")
	void updateOrderStatus(){
		//given
		var request = createOrder(1);
		Order order = request.toEntity(product, consumer);
		//when
		order.updateOrderStatus(OrderStatus.CANCEL);
		//then
		assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
	}

	@Test
	@DisplayName("order의 구매확정을 하면 주문 상태를 변경하는 메서드가 호출된다.")
	void confirmPurchaseTest() {
		Order order = spy(new Order());

		assertThatNoException()
			.isThrownBy(() -> order.confirmPurchase());

		then(order)
			.should(times(1))
			.updateOrderStatus(any());
	}

	@Nested
	@DisplayName("Order의 status가 ")
	class orderStatus{
		@Test
		@DisplayName("배송 완료가 아니면 false를 반환하고 배송 완료이면 true를 반환한다.")
		void isDeliveryCompletedTest() {
			//배송 완료 전
			OrderStatus statusDeliveryCompletedBefore = OrderStatus.ORDER_COMPLETED;
			Order orderDeliveryCompletedBefore = new Order();
			orderDeliveryCompletedBefore.updateOrderStatus(statusDeliveryCompletedBefore);

			assertThat(orderDeliveryCompletedBefore.isDeliveryCompleted()).isFalse();
			//배송 완료
			OrderStatus statusDeliveryCompleted = OrderStatus.DELIVERT_COMPLETED;
			Order orderDeliveryCompleted = new Order();
			orderDeliveryCompleted.updateOrderStatus(statusDeliveryCompleted);

			assertThat(orderDeliveryCompleted.isDeliveryCompleted()).isTrue();
		}


		@Test
		@DisplayName("배송 시작전이면 true를 반환하고 배송시작 이후면 false를 반환한다.")
		void isBeforeDeliveryStartTest() {
			//배송 시작 전
			OrderStatus statusDeliveryStartBefore = OrderStatus.ORDER_COMPLETED;
			Order orderDeliveryStartBefore = new Order();
			orderDeliveryStartBefore.updateOrderStatus(statusDeliveryStartBefore);

			assertThat(orderDeliveryStartBefore.isBeforeDeliveryStart()).isTrue();

			//배송 시작
			OrderStatus statusDeliveryStart= OrderStatus.DELIVERY_START;
			Order orderDeliveryStart = new Order();
			orderDeliveryStart.updateOrderStatus(statusDeliveryStart);

			assertThat(orderDeliveryStart.isBeforeDeliveryStart()).isFalse();

			//배송 시작 이후
			OrderStatus statusDeliveryStartAfter = OrderStatus.DELIVERT_COMPLETED;
			Order orderDeliveryStartAfter = new Order();
			orderDeliveryStartAfter.updateOrderStatus(statusDeliveryStartAfter);

			assertThat(orderDeliveryStartAfter.isBeforeDeliveryStart()).isFalse();
		}

		@Test
		@DisplayName("구매 확정이 아니면 false 반환하고 구매 확정 상태이면 true를 반환한다.")
		void isPurchaseConfirmedTest() {
			//구매 확정 전
			OrderStatus statusPurchaseConfirmedBefore = OrderStatus.DELIVERY_START;
			Order orderPurchaseConfirmedBefore = new Order();
			orderPurchaseConfirmedBefore.updateOrderStatus(statusPurchaseConfirmedBefore);

			assertThat(orderPurchaseConfirmedBefore.isPurchaseConfirmed()).isFalse();
			//배송 완료
			OrderStatus statusPurchaseConfirmed = OrderStatus.PURCHASE_CONFIRMATION;
			Order orderPurchaseConfirmed = new Order();
			orderPurchaseConfirmed.updateOrderStatus(statusPurchaseConfirmed);

			assertThat(orderPurchaseConfirmed.isPurchaseConfirmed()).isTrue();
		}

	}

	@Nested
	@DisplayName("배송 완료 메서드를 호출할 때 ")
	class completeOrderDelivery{
		@Test
		@DisplayName("배송 완료 시각을 매개변수로 받지 않으면 현재 시각을 배송 완료 시각으로 지정한다.")
		void testWithoutParam() {
			Order order = new Order();

			order.completeOrderDelivery();

			assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERT_COMPLETED);
			assertThat(order.getDeliveryCompleteTime()).isNotNull();
			assertThat(order.getDeliveryCompleteTime()).isBefore(LocalDateTime.now().plusSeconds(1));
		}

		@Test
		@DisplayName("배송 완료 시각을 매개변수로 받아 지정할 수 있다.")
		void testWithParam() {
			Order order = new Order();

			LocalDateTime deliveryTime = LocalDateTime.now().minusDays(1);
			order.completeOrderDelivery(deliveryTime);

			assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERT_COMPLETED);
			assertThat(order.getDeliveryCompleteTime()).isEqualTo(deliveryTime);
		}
	}

	OrderRequestDto createOrder(int count){
		return new OrderRequestDto(1L, 1000,1000L, 0L, count);
	}
}
