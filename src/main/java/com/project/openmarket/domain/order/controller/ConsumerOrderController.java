package com.project.openmarket.domain.order.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.auth.ConsumerThreadLocal;
import com.project.openmarket.domain.order.dto.request.OrderRequestDto;
import com.project.openmarket.domain.order.dto.response.OrderResponseDto;
import com.project.openmarket.domain.order.service.ConsumerOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/consumer")
public class ConsumerOrderController {
	private final ConsumerOrderService consumerOrderService;

	@GetMapping("/order")
	public ResponseEntity<List<OrderResponseDto>> getOrderList(){
		List<OrderResponseDto> responseDto = consumerOrderService.findOrderListByConsumer(ConsumerThreadLocal.get());
		return ResponseEntity.ok().body(responseDto);
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<OrderResponseDto> getOrderOne(@PathVariable("orderId")Long orderId){
		OrderResponseDto responseDto = consumerOrderService.findOrderOne(orderId);
		return ResponseEntity.ok().body(responseDto);
	}

	@PostMapping("/order")
	public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto) {
		OrderResponseDto responseDto = consumerOrderService.create(requestDto, ConsumerThreadLocal.get());
		return ResponseEntity.ok().body(responseDto);
	}

	@DeleteMapping("/order")
	public ResponseEntity<?> deleteOrderByConsumer(@RequestParam("orderId") Long orderId) {
		consumerOrderService.cancelOrder(orderId, ConsumerThreadLocal.get());
		return ResponseEntity.ok().body("success");
	}

	@GetMapping("/consumer/order")
	public ResponseEntity<?> confirmedOrder(@RequestParam("orderId") Long orderId){
		consumerOrderService.orderConfirmed(orderId, ConsumerThreadLocal.get());
		return ResponseEntity.ok().body("success");
	}
}
