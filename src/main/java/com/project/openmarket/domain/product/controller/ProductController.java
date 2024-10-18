package com.project.openmarket.domain.product.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.auth.SellerThreadLocal;
import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.dto.request.ProductUpdateReqeustDto;
import com.project.openmarket.domain.product.dto.response.ProductResponseDto;
import com.project.openmarket.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {
	private final ProductService productService;
	@GetMapping("/product/{productId}")
	public <T>ResponseEntity<ProductResponseDto> getProductById(@PathVariable("productId")Long productId) {
		ProductResponseDto responseDto = productService.findById(productId);
		return ResponseEntity.ok().body(responseDto);
	}

	@GetMapping("/product")
	public <T>ResponseEntity<List<ProductResponseDto>> searchProduct(
		@RequestParam("name") String productName,
		@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
	){
		List<ProductResponseDto> responseDtos = productService.findProductByName(productName, pageable);
		return ResponseEntity.ok().body(responseDtos);
	}

	@PostMapping("/seller/product")
	public <T>ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto requestDto) {
		ProductResponseDto responseDto = productService.create(requestDto, SellerThreadLocal.get());
		//TODO seller용 response 만들기
		return ResponseEntity.ok().body(responseDto);
	}

	@PutMapping("/seller/product")
	public <T>ResponseEntity<ProductResponseDto> update(@RequestBody ProductUpdateReqeustDto requestDto) {
		ProductResponseDto responseDto = productService.update(requestDto, SellerThreadLocal.get());
		return ResponseEntity.ok().body(responseDto);
	}

	@DeleteMapping("/seller/product/{productId}")
	public <T>ResponseEntity<?> delete(@PathVariable("productId")Long productId) {
		productService.delete(productId);
		return ResponseEntity.ok().body("success");
	}

	//TODO
	// - 상품 전체 조회


}
