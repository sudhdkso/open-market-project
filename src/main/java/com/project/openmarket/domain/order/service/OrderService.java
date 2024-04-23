package com.project.openmarket.domain.order.service;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final ConsumerRepository consumerRepository;

	//2. 주문 확인
	//2-1 개별 주문 확인 (order id)
	public Order findByOrderId(Long id){
		return orderRepository.findById(id)
			.orElseThrow(() -> new CustomException(NOT_FOUND_ORDER));
	}

	//3. 주문 취소 (주문 상태를 취소로 변경하고 주문 내역은 남겨 놓는다)
	@Transactional
	public void cancelOrder(Order order, Product product, Consumer consumer){

		product.increaseStock(order.getCount());
		productRepository.save(product);

		consumer.increaseAmount(order.getAmount());
		consumerRepository.save(consumer);

		order.updateOrderStatus(OrderStatus.CANCEL);
		orderRepository.save(order);
	}

}
