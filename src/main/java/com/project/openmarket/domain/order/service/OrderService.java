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
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.repository.ConsumerRepository;
import com.project.openmarket.domain.user.repository.SellerRepository;
import com.project.openmarket.global.exception.CustomException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final ConsumerRepository consumerRepository;
	private final SellerRepository sellerRepository;

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

	public void cancelOrder(Order order, Product product){
		Consumer consumer = consumerRepository.findById(order.getConsumer().getId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		cancelOrder(order, product, consumer);
	}

	@Transactional
	public void orderConfirmed(Order order, Seller seller, Consumer consumer){
		//주문 상태를 구매 확정 상태로 변경
		order.updateOrderStatus(OrderStatus.PURCHASE_CONFIRMATION);
		orderRepository.save(order);

		//판매자에게 수수료5% 제외한 금액 입..금?
		seller.increaseCache(calcRevenue(order.totalAmount()));
		sellerRepository.save(seller);

		//고객에게 2% 포인트 제공
		consumer.increasePoint(calcPoint(order.totalAmount()));
		consumerRepository.save(consumer);


	}

	//판매자 최종 수익 계산하는 함수
	private Long calcRevenue(Long amount){
		return amount - (long)Math.ceil((double)amount*0.05);
	}

	//고객에게 제공될 포인트 계산하는 함수
	private Long calcPoint(Long amount){
		return (long)Math.ceil((double)amount*0.02);
	}
}
