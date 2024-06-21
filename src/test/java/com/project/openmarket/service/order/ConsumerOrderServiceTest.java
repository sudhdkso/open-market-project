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

import com.project.openmarket.domain.order.dto.request.OrderRequestDto;
import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.service.ConsumerOrderService;
import com.project.openmarket.domain.order.service.OrderService;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.service.ServiceTestMock;

class ConsumerOrderServiceTest extends ServiceTestMock {
	@InjectMocks
	ConsumerOrderService consumerOrderService;
	@Mock
	ProductService productService;
	@Mock
	ConsumerService consumerService;
	@Mock
	OrderService orderService;

	@Nested
	@DisplayName("주문 생성 시 ")
	class createOrder{
		@Test
		@DisplayName("상품의 재고와 소지금이 충분하면 성공적으로 주문이 생성된다.")
		void createOrderWithEnoughStock(){
			//given
			var request = createOrder(1);

			//when
			given(productRepository.findByIdWithLock(anyLong())).willReturn(Optional.of(product));
			given(product.getPrice()).willReturn(request.orderedPrice());
			given(product.canBuy(anyInt())).willReturn(true);
			given(consumer.canBuy(any(Amount.class))).willReturn(true);

			//then
			assertThatNoException()
				.isThrownBy(() -> consumerOrderService.create(request, consumer));

			then(productService)
				.should(times(1))
				.decreaseProductStock(anyInt(), any(Product.class));
		}

		@Test
		@DisplayName("소지금은 충분해도 상품의 재고가 충분하지 않으면 오류가 발생한다.")
		void createOrderWithNotEnoughStock(){
			//given
			var request = createOrder(1);

			//when
			given(productRepository.findByIdWithLock(anyLong())).willReturn(Optional.of(product));
			given(product.getPrice()).willReturn(1000);
			given(product.isSoldOut()).willReturn(false);
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
			given(productRepository.findByIdWithLock(anyLong())).willReturn(Optional.of(product));
			given(product.getPrice()).willReturn(request.orderedPrice());
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
			given(productRepository.findByIdWithLock(anyLong())).willThrow(new CustomException(NOT_FOUND_PRODUCT));
			assertThatThrownBy(() -> consumerOrderService.create(request, consumer))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_FOUND_PRODUCT.getMessage());
		}
	}

	@Nested
	@DisplayName("주문 취소 시 ")
	class cancelOrder{
		@Test
		@DisplayName("주문, 상품, 고객이 모두 존재하면 주문이 성공적으로 취소된다.")
		void cancelOrderSuccess(){
			//given
			Long orderId = 1L;

			given(orderService.getOrderById(anyLong())).willReturn(order);

			given(order.getProduct()).willReturn(product);

			given(order.isBeforeDeliveryStart()).willReturn(true);

			//when
			assertThatNoException()
				.isThrownBy(() -> consumerOrderService.cancelOrder(orderId, consumer));


		}

		@Test
		@DisplayName("주문 상태가 배달 시작 이상이면 주문을 취소할 수 없다.")
		void cannotCancelOrderByOrderStatus(){
			//given
			Long orderId = 1L;

			given(orderService.getOrderById(anyLong())).willReturn(order);

			given(order.getProduct()).willReturn(product);

			given(order.isBeforeDeliveryStart()).willReturn(false);

			//when
			assertThatThrownBy(() -> consumerOrderService.cancelOrder(orderId, consumer))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(CANNOT_CANCLED_ORDER.getMessage());

		}
	}

	@Nested
	@DisplayName("구매 확정 시 ")
	class orderConfirmed{
		@Test
		@DisplayName("주문, 고객, 판매자가 모두 존재하며 주문 상태가 \"배송 완료\" 이면 성공한다.")
		void orderConfirmedSuccess(){
			Long orderId = 1L;
			given(orderService.getOrderById(anyLong())).willReturn(order);
			given(order.getSeller()).willReturn(seller);

			given(order.isDeliveryCompleted()).willReturn(true);

			assertThatNoException()
				.isThrownBy(() -> consumerOrderService.orderConfirmed(orderId, consumer));

			then(orderService)
				.should(times(1))
				.processConfirmedOrder(any(Order.class),any(Seller.class),any(Consumer.class));
		}

		@Test
		@DisplayName("주문, 고객, 판매자가 모두 존재하지만 주문 상태가 \"배송 완료\"가 아니면 오류가 발생한다.")
		void orderConfirmedFailByOrderStatus(){
			Long orderId = 1L;
			given(orderService.getOrderById(anyLong())).willReturn(order);
			given(order.isDeliveryCompleted()).willReturn(false);

			assertThatThrownBy(() -> consumerOrderService.orderConfirmed(orderId, consumer))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(CANNOT_CONFIRM_ORDER.getMessage());
		}
	}

	OrderRequestDto createOrder(int count){
		return new OrderRequestDto(1L,  1000,1000L, 0L, count);
	}
}
