package com.project.openmarket.service.order;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.project.openmarket.domain.order.dto.request.OrderRequestDto;
import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.service.ConsumerOrderService;
import com.project.openmarket.domain.order.service.OrderService;
import com.project.openmarket.service.ServiceTestMock;

class ConsumerOrderServiceTest extends ServiceTestMock {
	@InjectMocks
	private ConsumerOrderService consumerOrderService;
	@Mock
	private OrderService orderService;

	@Test
	@DisplayName("상품의 재고와 소지금이 충분하면 성공적으로 주문이 생성된다.")
	void createOrderWithEnoughStock(){
	    //given
	    var request = createOrder(1);

	    //when
	    given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
		given(product.canBuy(anyInt())).willReturn(true);
		given(consumer.canBuy(any(Amount.class))).willReturn(true);

	    //then
		assertThatNoException()
			.isThrownBy(() -> consumerOrderService.create(request, consumer));

		then(orderRepository)
			.should(times(1))
			.save(any(Order.class));
	}

	@Test
	@DisplayName("소지금은 충분해도 상품의 재고가 충분하지 않으면 오류가 발생한다.")
	void createOrderWithNotEnoughStock(){
		//given
		var request = createOrder(1);

		//when
		given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
		given(product.canBuy(anyInt())).willReturn(false);

		//then
		assertThatThrownBy(() -> consumerOrderService.create(request, consumer))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(NOT_ENOUGH_STOCK.getMessage());
	}

	@Test
	@DisplayName("상품의 재고가 충분해도 소지금이 충분하지 않으면 오류가 발생한다.")
	void createOrderWithNotEnoughCache(){
		//given
		var request = createOrder(1);

		//when
		given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
		given(product.canBuy(anyInt())).willReturn(true);
		given(consumer.canBuy(any(Amount.class))).willReturn(false);

		//then
		assertThatThrownBy(() -> consumerOrderService.create(request, consumer))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(NOT_ENOUTH_CACHE.getMessage());

	}

	@Test
	@DisplayName("존재하지 않는 상품으로 주문하면 오류가 발생한다.")
	void createOrderWithNotFoundProduct(){
		//given
		var request = createOrder(1);

		//when
		given(productRepository.findById(anyLong())).willReturn(Optional.empty());
		//then
		assertThatThrownBy(() -> consumerOrderService.create(request, consumer))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(NOT_FOUND_PRODUCT.getMessage());
	}

	@Test
	@DisplayName("주문, 상품, 고객이 모두 존재하면 주문이 성공적으로 취소된다.")
	void cancelOrder(){
	    //given
		Long orderId = 1L;

		given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

		given(order.getProduct()).willReturn(product);
		given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

		given(order.isBeforeDeliveryStart()).willReturn(true);
		//orderService.cancelOrder(order, product,  consumer);
	    //when
	    assertThatNoException()
			.isThrownBy(() -> consumerOrderService.cancelOrder(orderId, consumer));

	    //then
		then(orderRepository)
			.should(times(1))
			.save(any(Order.class));


	}

	@Test
	@DisplayName("주문 상태가 배달 시작 이상이면 주문을 취소할 수 없다.")
	void cannotCancelOrderByOrderStatus(){
		//given
		Long orderId = 1L;

		given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

		given(order.getProduct()).willReturn(product);
		given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

		given(order.isBeforeDeliveryStart()).willReturn(false);

		//when
		assertThatThrownBy(() -> consumerOrderService.cancelOrder(orderId, consumer))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(CANNOT_CANCLED_ORDER.getMessage());

	}

	@Test
	@DisplayName("유효한 주문 id를 가지고 주문을 조회하면 성공한다.")
	void findByValidOrderId(){
	    given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

	    assertThatNoException()
			.isThrownBy(() -> consumerOrderService.findByOrderId(1L));

	}

	@Test
	@DisplayName("유효하지 않은 주문 id를 가지고 주문을 조회하면 오류가 발생한다..")
	void findByInvalidOrderId(){
		given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

		assertThatThrownBy(() -> consumerOrderService.findByOrderId(1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(NOT_FOUND_ORDER.getMessage());


	}

	OrderRequestDto createOrder(int count){
		return new OrderRequestDto(1L, "주문 완료", 1000L, 0L, count);
	}
}
