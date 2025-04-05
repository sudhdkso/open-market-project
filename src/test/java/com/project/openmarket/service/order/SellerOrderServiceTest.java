package com.project.openmarket.service.order;

import static com.project.openmarket.domain.order.entity.eums.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.project.openmarket.domain.order.dto.response.OrderListResponsesDto;
import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.service.OrderService;
import com.project.openmarket.domain.order.service.SellerOrderService;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.product.service.SellerProductService;
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
	@Mock
	SellerProductService sellerProductService;

	@DisplayName("주문이 유효하면 주문 상태를 성공적으로 변경할 수 있다.")
	@Test
	void whenOrderIsValid_thenOrderStatusIsUpdated() {
		//given
		Long orderId = 1L;
		given(orderService.getOrderById(any())).willReturn(order);
		//when
		assertThatNoException()
			.isThrownBy(() -> sellerOrderService.updateOrderStatus(order.getId(), SHIPPING.getValue()));
		//then
		verify(order, times(1)).updateOrderStatus(SHIPPING);
		verify(orderRepository, times(1)).save(any(Order.class));
	}

	@DisplayName("order, product, consumer가 모두 유효하면 주문을 성공적으로 취소할 수 있다.")
	@Test
	void whenAllValid_thenCancelOrderSuccess() {
		ObjectId orderId = new ObjectId("67ec1324da973979b3723d17");
		given(orderService.getOrderById(any())).willReturn(order);

		ObjectId productId = new ObjectId("67ecf53ea1bbd10495c3ef12");
		given(order.getProduct()).willReturn(product);
		given(product.getId()).willReturn(productId);
		given(productService.getProductById(any())).willReturn(product);

		given(consumerService.getConsumerById(any())).willReturn(consumer);

		assertThatNoException()
			.isThrownBy(() -> sellerOrderService.cancelOrder(orderId.toHexString()));

		verify(orderService, times(1)).processOrderCancel(any(Order.class), any(Product.class), any(Consumer.class));
	}

	@DisplayName("판매자 주문이 있는 경우 findOrdersBySellerId는 orderList를 return한다.")
	@Test
	void whenSellerHasOrders_thenReturnOrderList() {
		// given
		SellerCreateRequestDto sellerReuestDto = new SellerCreateRequestDto("seller@example.com", "seller",
			"010-0000-0000", "1234");
		Seller seller = spy(Seller.of(sellerReuestDto));

		Product product1 = spy(new Product("Laptop Pro", "", 12000, 1, seller));
		Product product2 = spy(new Product("Laptop Air", "", 999, 2, seller));

		List<Product> products = List.of(product1, product2);

		Order order1 = new Order(product1, null, new Amount(1000L, 0L), 0, consumer);
		Order order2 = new Order(product2, null, new Amount(0L, 1000L), 0, consumer);

		List<Order> orders = Arrays.asList(order1, order2);

		given(sellerProductService.findAllBySeller(any())).willReturn(products);
		given(product1.getId()).willReturn(new ObjectId("67ec1324da973979b3723d17"));
		given(product2.getId()).willReturn(new ObjectId("67ecf4f1242eb508da029fc7"));

		when(orderRepository.findBySellerProducts(any())).thenReturn(orders);

		// when
		OrderListResponsesDto result = sellerOrderService.findOrdersBySeller(seller);

		// then
		assertThat(result.orders()).hasSize(2);

		verify(sellerProductService, times(1)).findAllBySeller(any(Seller.class));
		verify(orderRepository, times(1)).findBySellerProducts(any());
	}

	@Test
	@DisplayName("판매자 주문이 없는 경우 findOrdersBySellerId는 빈 리스트를 return한다.")
	void whenSellerHasNoOrders_thenReturnEmptyList() {

		Product product1 = spy(new Product("Laptop Pro", "", 12000, 1, seller));
		Product product2 = spy(new Product("Laptop Air", "", 999, 2, seller));

		List<Product> products = List.of(product1, product2);

		given(sellerProductService.findAllBySeller(any())).willReturn(products);
		given(product1.getId()).willReturn(new ObjectId("67ec1324da973979b3723d17"));
		given(product2.getId()).willReturn(new ObjectId("67ecf4f1242eb508da029fc7"));

		when(orderRepository.findBySellerProducts(any())).thenReturn(List.of());

		OrderListResponsesDto result = sellerOrderService.findOrdersBySeller(seller);

		assertTrue(result.orders().isEmpty());

		verify(sellerProductService, times(1)).findAllBySeller(any(Seller.class));
		verify(orderRepository, times(1)).findBySellerProducts(any());
	}
}
