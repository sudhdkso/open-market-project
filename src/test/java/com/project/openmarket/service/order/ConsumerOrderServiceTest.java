package com.project.openmarket.service.order;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.project.openmarket.domain.order.dto.request.OrderRequestDto;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.service.ConsumerOrderService;
import com.project.openmarket.service.ServiceTestMock;

class ConsumerOrderServiceTest extends ServiceTestMock {
	@InjectMocks
	private ConsumerOrderService consumerOrderService;

	@Test
	@DisplayName("상품의 재고가 충분하면 성공적으로 주문이 생성된다.")
	void createOrderWithEnoughStock(){
	    //given
	    var request = createOrder(1);

	    //when
	    given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
		given(product.canBuy(anyInt())).willReturn(true);

	    //then
		assertThatNoException()
			.isThrownBy(() -> consumerOrderService.create(request, consumer));

		then(orderRepository)
			.should(times(1))
			.save(any(Order.class));
	}

	@Test
	@DisplayName("상품의 재고가 충분하지 않으면 오류가 발생한다.")
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

	OrderRequestDto createOrder(int count){
		return new OrderRequestDto(1L, "주문 완료", count);
	}
}
