package com.project.openmarket.domain.product.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.openmarket.domain.product.dto.response.ProductListResponsesDto;
import com.project.openmarket.domain.product.dto.response.ProductResponseDto;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	/**
	 * 상품을 조회하는 메소드
	 * @param productId 조회하려는 상품의 id
	 * @return 조회한 상품의 정보
	 */
	public ProductResponseDto findById(String productId) {
		Product product = getProductById(new ObjectId(productId));
		return ProductResponseDto.of(product);
	}

	public List<Product> findByScoreDesc(String name) {
		return productRepository.findByNameContainsOrderByAvgScoreDesc(name);
	}

	public Page<ProductResponseDto> findProductByName(String name, Pageable pageable) {
		return productRepository.findByNameContaining(name, pageable)
			.map(ProductResponseDto::of);
	}

	public ProductListResponsesDto findAllProduct() {
		return ProductListResponsesDto.of(productRepository.findAll());
	}

	public void updateProductAvgScore(double avgScore, Product product) {
		product.updateAvgScore(avgScore);
		productRepository.save(product);
	}

	public Product getProductById(ObjectId id) {
		return productRepository.getById(id);
	}

	public void increaseProductStock(int count, Product product) {
		product.increaseStock(count);
		productRepository.save(product);
	}

	public void decreaseProductStock(int count, Product product) {
		product.decreaseStock(count);
		productRepository.save(product);
	}

}
