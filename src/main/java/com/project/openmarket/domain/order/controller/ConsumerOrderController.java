package com.project.openmarket.domain.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.order.dto.request.OrderRequestDto;
import com.project.openmarket.domain.order.dto.response.OrderDetailResponseDto;
import com.project.openmarket.domain.order.dto.response.OrderListResponsesDto;
import com.project.openmarket.domain.order.dto.response.OrderResponseDto;
import com.project.openmarket.domain.order.service.ConsumerOrderService;
import com.project.openmarket.global.context.ConsumerThreadLocal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumer")
public class ConsumerOrderController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ConsumerOrderService consumerOrderService;

	@GetMapping("/orders")
	public ResponseEntity<OrderListResponsesDto> getOrderList() {
		OrderListResponsesDto responseDto = consumerOrderService.findOrderListByConsumer(ConsumerThreadLocal.get());
		return ResponseEntity.ok().body(responseDto);
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<OrderDetailResponseDto> getOrderOne(@PathVariable("orderId") String orderId) {
		OrderDetailResponseDto responseDto = consumerOrderService.findOrderOne(orderId);
		return ResponseEntity.ok().body(responseDto);
	}

	@PostMapping("/purchase/{productId}")
	public ResponseEntity<OrderResponseDto> createOrder(@PathVariable("productId") String productId,
		@RequestBody OrderRequestDto requestDto) {
		logger.info("물건 주문!");
		OrderResponseDto responseDto = consumerOrderService.create(productId, requestDto, ConsumerThreadLocal.get());
		return ResponseEntity.ok().body(responseDto);
	}

	@DeleteMapping("/order")
	public ResponseEntity<?> deleteOrderByConsumer(@RequestParam("orderId") String orderId) {
		consumerOrderService.cancelOrder(orderId, ConsumerThreadLocal.get());
		return ResponseEntity.ok().body("success");
	}

	@GetMapping("/consumer/order")
	public ResponseEntity<?> confirmedOrder(@RequestParam("orderId") String orderId) {
		consumerOrderService.orderConfirmed(orderId, ConsumerThreadLocal.get());
		return ResponseEntity.ok().body("success");
	}
}
