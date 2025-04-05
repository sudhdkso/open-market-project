package com.project.openmarket.domain.product.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.product.dto.response.ProductListResponsesDto;
import com.project.openmarket.domain.product.dto.response.ProductResponseDto;
import com.project.openmarket.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {
	private final ProductService productService;

	@GetMapping("/product/{productId}")
	public <T> ResponseEntity<ProductResponseDto> getProductById(@PathVariable("productId") String productId) {
		ProductResponseDto responseDto = productService.findById(productId);
		return ResponseEntity.ok().body(responseDto);
	}

	@GetMapping("/products")
	public <T> ResponseEntity<ProductListResponsesDto> getAllProduct() {
		ProductListResponsesDto responseDto = productService.findAllProduct();
		return ResponseEntity.ok().body(responseDto);
	}

	@GetMapping("/search/products")
	public <T> ResponseEntity<Page<ProductResponseDto>> searchProduct(
		@RequestParam("name") String productName,
		@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<ProductResponseDto> responseDtos = productService.findProductByName(productName, pageable);
		return ResponseEntity.ok().body(responseDtos);
	}

}
