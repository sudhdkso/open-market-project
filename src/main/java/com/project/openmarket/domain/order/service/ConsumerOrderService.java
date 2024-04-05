package com.project.openmarket.domain.order.service;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.order.dto.request.OrderRequestDto;
import com.project.openmarket.domain.order.dto.response.OrderResponseDto;
import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;
import com.project.openmarket.domain.order.repository.OrderRepository;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.repository.ProductRepository;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.repository.ConsumerRepository;
import com.project.openmarket.global.exception.CustomException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConsumerOrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final ConsumerRepository consumerRepository;

	//1. 주문 생성
	@Transactional
	public OrderResponseDto create(OrderRequestDto request, Consumer consumer){
		Product product = productRepository.findById(request.productId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

		purchase(request, consumer);

		Order order = orderRepository.save(Order.of(product, request, consumer));
		return new OrderResponseDto(order);
	}

	private void purchase(OrderRequestDto request, Consumer consumer){
		Product product = productRepository.findById(request.productId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

		Amount amount = new Amount(request.cache(), request.point());
		int count = request.count();

		checkEnoughStock(product, count);
		checkEnoughTotalCache(consumer, amount);

		product.decreaseStock(count);
		productRepository.save(product);

		consumer.decreaseAmount(amount);
		consumerRepository.save(consumer);
	}

	private void checkEnoughStock( Product product, int count){
		if(!product.canBuy(count)){
			throw new CustomException(NOT_ENOUGH_STOCK);
		}
	}

	private void checkEnoughTotalCache(Consumer consumer, Amount amount){
		if(!consumer.canBuy(amount)){
			throw new CustomException(NOT_ENOUTH_CACHE);
		}
	}

	//2. 주문 확인
	//2-1 개별 주문 확인 (order id)
	public Order findByOrderId(Long id){
		return orderRepository.findById(id)
			.orElseThrow(() -> new CustomException(NOT_FOUND_ORDER));
	}

	//3. 주문 취소 (주문 상태를 취소로 변경하고 주문 내역은 남겨 놓는다)
	public void cancelOrder(Long id){
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new CustomException(NOT_FOUND_ORDER));
		Product product = productRepository.findById(order.getProduct().getId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
		Consumer consumer = order.getConsumer();

		product.increaseStock(order.getCount());
		productRepository.save(product);

		consumer.increaseAmount(order.getAmount());
		consumerRepository.save(consumer);

		order.updateOrderStatus(OrderStatus.CANCEL);
		orderRepository.save(order);
	}

	//4. 주문 수정
}
