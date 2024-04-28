package com.project.openmarket.domain.order.service;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;
import com.project.openmarket.domain.order.repository.OrderRepository;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.domain.user.service.SellerService;
import com.project.openmarket.global.exception.CustomException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductService productService;
	private final ConsumerService consumerService;
	private final SellerService sellerService;

	//2. 주문 확인
	//2-1 개별 주문 확인 (order id)
	public Order getOrderById(Long id) {
		return orderRepository.findById(id)
			.orElseThrow(() -> new CustomException(NOT_FOUND_ORDER));
	}

	private void cancelOrderStatus(Order order){
		order.updateOrderStatus(OrderStatus.CANCEL);
		orderRepository.save(order);
	}

	//3. 주문 취소 (주문 상태를 취소로 변경하고 주문 내역은 남겨 놓는다)
	@Transactional
	public void processOrderCancel(Order order, Product product, Consumer consumer) {
		//재고 추가
		productService.increaseProductStock(order.getCount(), product);
		//고객 소지금 증가
		consumerService.increaseAmount(order.getAmount(), consumer);
		//주문 상태 취소로 변경
		cancelOrderStatus(order);
	}

	private void orderConfirmed(Order order){
		order.confirmPurchase();
		orderRepository.save(order);
	}

	@Transactional
	public void processConfirmedOrder(Order order, Seller seller, Consumer consumer) {
		//주문 상태를 구매 확정 상태로 변경
		orderConfirmed(order);

		//판매자에게 수수료5% 제외한 금액 입금
		sellerService.processPayment(order.totalAmount(), seller);

		//고객에게 2% 포인트 제공
		consumerService.processPoints(order.totalAmount(), consumer);
	}

}
