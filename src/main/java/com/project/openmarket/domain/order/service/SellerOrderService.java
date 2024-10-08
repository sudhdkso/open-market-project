package com.project.openmarket.domain.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.openmarket.domain.order.dto.response.OrderResponseDto;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;
import com.project.openmarket.domain.order.repository.OrderRepository;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.service.ConsumerService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SellerOrderService {
	private final OrderService orderService;
	private final OrderRepository orderRepository;
	private final ProductService productSerivce;
	private final ConsumerService consumerService;

	//1. 주문 상태 변경
	public void updateOrderStatus(Long id, String status){
		Order order = orderService.getOrderById(id);
		order.updateOrderStatus(OrderStatus.getOrderStatus(status));
		orderRepository.save(order);
	}

	//2. 판매자가 주문 취소하는 함수
	public void cancelOrder(Long id){
		Order order = orderService.getOrderById(id);
		Product product = productSerivce.getProductById(order.getProduct().getId());
		Consumer consumer = consumerService.getConsumerById(order.getConsumer().getId());

		orderService.processOrderCancel(order, product, consumer);
	}

	public List<OrderResponseDto> findOrdersBySeller(Seller seller) {
		return orderRepository.findOrdersBySellerId(seller.getId())
			.stream()
			.map(order -> new OrderResponseDto(order))
			.toList();
	}

}
