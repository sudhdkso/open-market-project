package com.project.openmarket.domain.order.service;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.order.dto.response.OrderListResponsesDto;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;
import com.project.openmarket.domain.order.repository.OrderRepository;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.product.service.SellerProductService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.service.ConsumerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerOrderService {
	private final OrderService orderService;
	private final OrderRepository orderRepository;
	private final ProductService productSerivce;
	private final ConsumerService consumerService;
	private final SellerProductService sellerProductService;

	//1. 주문 상태 변경
	public void updateOrderStatus(String id, String status) {
		Order order = orderService.getOrderById(id);
		order.updateOrderStatus(OrderStatus.getOrderStatus(status));
		orderRepository.save(order);
	}

	//2. 판매자가 주문 취소하는 함수
	public void cancelOrder(String id) {
		Order order = orderService.getOrderById(id);
		Product product = productSerivce.getProductById(order.getProduct().getId());
		Consumer consumer = consumerService.getConsumerById(order.getConsumerId());

		orderService.processOrderCancel(order, product, consumer);
	}

	@Transactional(readOnly = true)
	public OrderListResponsesDto findOrdersBySeller(Seller seller) {
		List<Product> sellerProducts = sellerProductService.findAllBySeller(seller);

		List<ObjectId> productIds = sellerProducts.stream()
			.map(Product::getId)
			.collect(Collectors.toList());

		List<Order> orders = orderRepository.findBySellerProducts(productIds);

		return OrderListResponsesDto.of(orders);

	}

}
