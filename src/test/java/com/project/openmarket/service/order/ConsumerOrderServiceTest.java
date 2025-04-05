package com.project.openmarket.service.order;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.project.openmarket.domain.order.dto.request.OrderRequestDto;
import com.project.openmarket.domain.order.dto.response.OrderDetailResponseDto;
import com.project.openmarket.domain.order.dto.response.OrderListResponsesDto;
import com.project.openmarket.domain.order.dto.response.OrderResponseDto;
import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;
import com.project.openmarket.domain.order.service.ConsumerOrderService;
import com.project.openmarket.domain.order.service.OrderService;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.domain.user.service.SellerService;
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
	@Mock
	SellerService sellerService;

	@Nested
	@DisplayName("주문 생성 시 ")
	class createOrder {
		@DisplayName("상품의 재고와 소지금이 충분하면 성공적으로 주문이 생성된다.")
		@Test
		void whenStockAndBalanceAreSufficient_thenCreateOrderSuccess() {
			//given
			var request = createOrder(1);
			String productId = "67ec1324da973979b3723d17";
			Product product = spy(new Product("product", "description", 1000, 1, seller));
			Order order = request.toEntity(product, consumer);
			given(productRepository.getById(any())).willReturn(product);
			given(consumer.canBuy(any(Amount.class))).willReturn(true);

			given(orderRepository.save(any())).willReturn(order);

			given(product.getId()).willReturn(new ObjectId(productId));
			given(product.getSeller()).willReturn(seller);
			given(seller.getId()).willReturn(new ObjectId("67ea40df5aeb2844f05b84e8"));

			//when
			OrderResponseDto response = consumerOrderService.create(productId, request, consumer);

			//then
			verify(productService, times(1)).decreaseProductStock(anyInt(), any(Product.class));
		}

		@DisplayName("소지금은 충분해도 상품의 재고가 충분하지 않으면 오류가 발생한다.")
		@Test
		void whenStockIsInsufficient_thenThrowException() {
			//given
			var request = createOrder(1);
			String productId = "67ec1324da973979b3723d17";

			given(productRepository.getById(any())).willReturn(product);
			given(product.getPrice()).willReturn(1000);
			given(product.isSoldOut()).willReturn(false);
			given(product.canBuy(anyInt())).willReturn(false);

			//when & then
			assertThatThrownBy(() -> consumerOrderService.create(productId, request, consumer))
				.isInstanceOf(CustomException.class)
				.hasMessage(NOT_ENOUGH_STOCK.getMessage());
		}

		@DisplayName("상품의 재고가 충분해도 소지금이 충분하지 않으면 오류가 발생한다.")
		@Test
		void whenCashIsInsufficient_thenThrowException() {
			//given
			var request = createOrder(1);

			String productId = "67ec1324da973979b3723d17";

			given(productRepository.getById(any())).willReturn(product);
			given(product.getPrice()).willReturn(request.orderedPrice());
			given(product.canBuy(anyInt())).willReturn(true);
			given(consumer.canBuy(any(Amount.class))).willReturn(false);

			//when & then
			assertThatThrownBy(() -> consumerOrderService.create(productId, request, consumer))
				.isInstanceOf(CustomException.class)
				.hasMessage(NOT_ENOUTH_CACHE.getMessage());

		}
	}

	@Nested
	@DisplayName("주문 취소 시 ")
	class cancelOrder {
		@DisplayName("주문, 상품, 고객이 모두 존재할 때 주문이 성공적으로 취소된다.")
		@Test
		void whenOrderProductAndConsumerExist_thenCancelOrderSuccess() {
			//given
			String orderId = "aaa";

			given(orderService.getOrderById(any())).willReturn(order);

			given(order.getProduct()).willReturn(product);

			given(order.isBeforeDeliveryStart()).willReturn(true);

			//when
			assertThatNoException()
				.isThrownBy(() -> consumerOrderService.cancelOrder(orderId, consumer));

		}

		@DisplayName("주문 상태가 배송 중 이상이면 주문을 취소할 수 없다.")
		@Test
		void whenOrderStatusIsAtLeastShipping_thenCannotCancelOrder() {
			//given
			String orderId = "aaa";

			given(orderService.getOrderById(any())).willReturn(order);

			given(order.getProduct()).willReturn(product);

			given(order.isBeforeDeliveryStart()).willReturn(false);

			//when
			assertThatThrownBy(() -> consumerOrderService.cancelOrder(orderId, consumer))
				.isInstanceOf(CustomException.class)
				.hasMessage(CANNOT_CANCLED_ORDER.getMessage());

		}
	}

	@Nested
	@DisplayName("구매 확정 시 ")
	class orderConfirmed {
		@DisplayName("주문, 고객, 판매자가 모두 존재하고 주문 상태가 배송 완료일 때 주문 확인에 성공한다.")
		@Test
		void whenOrderIsDeliveredAndAllExist_thenConfirmOrderSuccess() {
			String orderId = "67ea40df5aeb2844f05b84e8";
			given(orderService.getOrderById(any())).willReturn(order);
			given(order.getSellerId()).willReturn(new ObjectId(orderId));

			given(order.isDeliveryCompleted()).willReturn(true);
			given(sellerService.findById(any())).willReturn(seller);

			assertThatNoException()
				.isThrownBy(() -> consumerOrderService.orderConfirmed(orderId, consumer));

			then(orderService)
				.should(times(1))
				.processConfirmedOrder(any(Order.class), any(Seller.class), any(Consumer.class));
		}

		@DisplayName("주문, 고객, 판매자가 모두 존재하지만 주문 상태가 배송 완료가 아닐 경우 주문 확인에 실패한다.")
		@Test
		void whenOrderStatusIsNotDelivered_thenConfirmOrderFails() {
			String orderId = "67ea40df5aeb2844f05b84e8";
			given(orderService.getOrderById(any())).willReturn(order);
			given(order.isDeliveryCompleted()).willReturn(false);

			assertThatThrownBy(() -> consumerOrderService.orderConfirmed(orderId, consumer))
				.isInstanceOf(CustomException.class)
				.hasMessage(CANNOT_CONFIRM_ORDER.getMessage());
		}
	}

	@DisplayName("유효한 주문 ID로 주문 상세 정보를 조회할 수 있다.")
	@Test
	void whenOrderIdIsValid_thenReturnOrderDetail() {
		//given
		String orderId = "67eff36cdfbc3039fb20924a";
		given(consumer.getAddress()).willReturn("address");

		Order order = new Order(product, OrderStatus.ORDER_COMPLETED, new Amount(10000L, 0L), 1, consumer);
		given(orderService.getOrderById(any())).willReturn(order);
		given(product.getName()).willReturn("product1");
		//when
		OrderDetailResponseDto response = consumerOrderService.findOrderOne(orderId);
		//then
		assertThat(response).isNotNull();
		assertThat(response.productName()).isEqualTo("product1");
		assertThat(response.address()).isEqualTo("address");

		verify(orderService, times(1)).getOrderById(any());
	}

	@DisplayName("소비자의 모든 주문 내역을 조회할 수 있다.")
	@Test
	void whenConsumerExists_thenReturnOrderList() {
		//given
		Order order1 = new Order(product, OrderStatus.ORDER_COMPLETED, new Amount(1000L, 0L), 1, consumer);
		Order order2 = new Order(product, OrderStatus.SHIPPING, new Amount(1000L, 0L), 1, consumer);
		given(orderRepository.findByConsumer(any())).willReturn(List.of(order1, order2));
		given(product.getName()).willReturn("product");
		//when
		OrderListResponsesDto reponses = consumerOrderService.findOrderListByConsumer(consumer);
		//then
		assertThat(reponses.orders()).hasSize(2);
		assertThat(reponses.orders().get(0).status()).isEqualTo(OrderStatus.ORDER_COMPLETED);
		assertThat(reponses.orders().get(1).status()).isEqualTo(OrderStatus.SHIPPING);

		verify(orderRepository, times(1)).findByConsumer(any(Consumer.class));
	}

	OrderRequestDto createOrder(int count) {
		return new OrderRequestDto(1000, 1000L, 0L, count);
	}
}
