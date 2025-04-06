package com.project.openmarket.domain.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.dto.request.ProductUpdateReqeustDto;
import com.project.openmarket.domain.product.dto.response.ProductCreateResponseDto;
import com.project.openmarket.domain.product.dto.response.ProductResponseDto;
import com.project.openmarket.domain.product.dto.response.SellerProductResponsesDto;
import com.project.openmarket.domain.product.service.SellerProductService;
import com.project.openmarket.global.context.SellerThreadLocal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
public class SellerProductController {
	private final SellerProductService sellerProductService;

	@PostMapping("/product")
	public <T> ResponseEntity<ProductCreateResponseDto> create(@RequestBody ProductRequestDto requestDto) {
		ProductCreateResponseDto responseDto = sellerProductService.create(requestDto, SellerThreadLocal.get());
		//TODO seller용 response 만들기
		return ResponseEntity.ok().body(responseDto);
	}

	@GetMapping("/products")
	public <T> ResponseEntity<SellerProductResponsesDto> getAllProductsBySeller() {
		SellerProductResponsesDto responseDto = sellerProductService.findAllProductBySeller(SellerThreadLocal.get());
		return ResponseEntity.ok().body(responseDto);
	}

	@PutMapping("/product/{productId}")
	public <T> ResponseEntity<ProductResponseDto> update(@PathVariable("productId") String productId,
		@RequestBody ProductUpdateReqeustDto requestDto) {
		ProductResponseDto responseDto = sellerProductService.update(productId, requestDto, SellerThreadLocal.get());
		return ResponseEntity.ok().body(responseDto);
	}

	@DeleteMapping("/product/{productId}")
	public <T> ResponseEntity<?> delete(@PathVariable("productId") String productId) {
		sellerProductService.delete(productId);
		return ResponseEntity.ok().body("success");
	}
}
