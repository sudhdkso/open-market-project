package com.project.openmarket.domain.order.service;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.order.dto.request.OrderRequestDto;
import com.project.openmarket.domain.order.dto.response.OrderDetailResponseDto;
import com.project.openmarket.domain.order.dto.response.OrderListResponsesDto;
import com.project.openmarket.domain.order.dto.response.OrderResponseDto;
import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.repository.OrderRepository;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.repository.ProductRepository;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.domain.user.service.SellerService;
import com.project.openmarket.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsumerOrderService {
	private final OrderService orderService;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final ProductService productService;
	private final ConsumerService consumerService;
	private final SellerService sellerService;

	//1. 주문 생성
	@Transactional
	public OrderResponseDto create(String productId, OrderRequestDto request, Consumer consumer) {
		Product product = productRepository.getById(new ObjectId(productId));

		Order order = request.toEntity(product, consumer);

		checkPriceMismatch(order, product.getPrice());

		purchase(request, product, consumer);
		return OrderResponseDto.of(orderRepository.save(order));
	}

	private void purchase(OrderRequestDto request, Product product, Consumer consumer) {

		Amount amount = new Amount(request.cash(), request.point());
		int count = request.count();

		checkEnoughStock(product, count);
		checkEnoughTotalCache(consumer, amount);
		//1. 재고 감소
		productService.decreaseProductStock(count, product);

		//2.고객 소지금 감소
		consumerService.decreaseAmount(amount, consumer);
	}

	private void checkPriceMismatch(Order order, int productPrice) {
		if (!order.isEqualPriceTo(productPrice)) {
			throw new CustomException(NOT_MATCH_PRICE);
		}
	}

	private void checkEnoughStock(Product product, int count) {
		if (product.isSoldOut() || !product.canBuy(count)) {
			throw new CustomException(NOT_ENOUGH_STOCK);
		}
	}

	private void checkEnoughTotalCache(Consumer consumer, Amount amount) {
		if (!consumer.canBuy(amount)) {
			throw new CustomException(NOT_ENOUTH_CACHE);
		}
	}

	//3. 주문 취소 (주문 상태를 취소로 변경하고 주문 내역은 남겨 놓는다), 배송 출발 전까지만 가능
	public void cancelOrder(String id, Consumer consumer) {
		Order order = orderService.getOrderById(id);
		Product product = order.getProduct();

		if (!order.isBeforeDeliveryStart()) {
			throw new CustomException(CANNOT_CANCLED_ORDER);
		}

		orderService.processOrderCancel(order, product, consumer);
	}

	//4. 구매 확정(배송 완료된 주문에 대해서만 변경 가능)
	//판매자에게 수익의 5%의 수수료를 제외하고 입금, 고객에게 2%의 포인트 제공
	public void orderConfirmed(String id, Consumer consumer) {
		Order order = orderService.getOrderById(id);

		//배송이 완료된 주문에서만 구매 확정 가능
		if (!order.isDeliveryCompleted()) {
			throw new CustomException(CANNOT_CONFIRM_ORDER);
		}
		Seller seller = sellerService.findById(order.getSellerId());
		orderService.processConfirmedOrder(order, seller, consumer);
	}

	public OrderDetailResponseDto findOrderOne(String id) {
		return OrderDetailResponseDto.of(orderService.getOrderById(id));
	}

	public OrderListResponsesDto findOrderListByConsumer(Consumer consumer) {
		List<Order> order = orderRepository.findByConsumer(consumer);
		return OrderListResponsesDto.of(order);
	}
}
