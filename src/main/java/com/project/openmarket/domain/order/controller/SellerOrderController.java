package com.project.openmarket.domain.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.order.service.SellerOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
public class SellerOrderController {
	private final SellerOrderService sellerOrderService;

	//TODO : 판매자 주문 조회

	@DeleteMapping("/order")
	public ResponseEntity<?> deleteOrderBySeller(@RequestParam("orderId") Long orderId) {
		sellerOrderService.cancelOrder(orderId);
		return ResponseEntity.ok().body("success");
	}
}
