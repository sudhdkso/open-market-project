package com.project.openmarket.domain.order.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.auth.SellerThreadLocal;
import com.project.openmarket.domain.order.dto.response.OrderResponseDto;
import com.project.openmarket.domain.order.service.SellerOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
public class SellerOrderController {
	private final SellerOrderService sellerOrderService;

	//TODO : 판매자 주문 조회
	@GetMapping("/order")
	public ResponseEntity<List<OrderResponseDto>> getOrdersBySeller(){
		List<OrderResponseDto> responseDtos = sellerOrderService.findOrdersBySeller(SellerThreadLocal.get());
		return ResponseEntity.ok().body(responseDtos);
	}

	@DeleteMapping("/order")
	public ResponseEntity<?> deleteOrderBySeller(@RequestParam("orderId") Long orderId) {
		sellerOrderService.cancelOrder(orderId);
		return ResponseEntity.ok().body("success");
	}
}
