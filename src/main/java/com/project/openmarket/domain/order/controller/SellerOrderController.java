package com.project.openmarket.domain.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.auth.SellerThreadLocal;
import com.project.openmarket.domain.order.dto.response.OrderListResponsesDto;
import com.project.openmarket.domain.order.service.SellerOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
public class SellerOrderController {
	private final SellerOrderService sellerOrderService;

	@GetMapping("/orders")
	public ResponseEntity<OrderListResponsesDto> getOrdersBySeller() {
		OrderListResponsesDto responsesDto = sellerOrderService.findOrdersBySeller(SellerThreadLocal.get());
		return ResponseEntity.ok().body(responsesDto);
	}

	@DeleteMapping("/order/{orderId}")
	public ResponseEntity<?> deleteOrderBySeller(@PathVariable("orderId") String orderId) {
		sellerOrderService.cancelOrder(orderId);
		return ResponseEntity.ok().body("success");
	}
}
