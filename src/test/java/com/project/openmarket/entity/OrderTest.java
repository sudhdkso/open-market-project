package com.project.openmarket.entity;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
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
		Order order = Order.of(product, request, consumer);
		//when
		order.updateOrderStatus(OrderStatus.CANCEL);
		//then
		assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
	}

	OrderRequestDto createOrder(int count){
		return new OrderRequestDto(1L, "주문 완료",1000L, 0L, count);

	}
}
