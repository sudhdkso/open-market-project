package com.project.openmarket.domain.product.service;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import org.springframework.stereotype.Service;

import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.dto.request.ProductUpdateReqeustDto;
import com.project.openmarket.domain.product.dto.response.ProductResponseDto;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.repository.ProductRepository;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.service.SellerService;
import com.project.openmarket.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final SellerService sellerService;

	/**
	 * 상품을 등록하는 메소드
	 * @param request 등록할 상품의 정보
	 * @param seller 상품을 등록하려는 판매자
	 * @return 등록된 상품의 정보
	 */
	public ProductResponseDto create(ProductRequestDto request, Seller seller){
		//상품 이름과 판매자로 일치하는 상품이 있는 경우 예외 발생
		duplicateProduct(request.name(), seller);
		Product product = productRepository.save(Product.of(request, seller));
		return ProductResponseDto.of(product);
	}

	/**
	 * 상품을 업데이트하는 메소드
	 * @param request 업데이트할 상품의 정보
	 * @param seller 상품을 업데이터하려는 판매자
	 * @return 업데이트된 상품의 정보
	 */
	public ProductResponseDto update(ProductUpdateReqeustDto request, Seller seller){
		Product product = productRepository.findById(request.id())
			.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
		//기존 상품의 이름과 수정하려는 상품의 이름이 다른 경우에만 확인
		if(!product.isSameName(request.name())){
			duplicateProduct(request.name(), seller);
		}
		product.update(request);

		return ProductResponseDto.of(product);
	}

	/**
	 * 상품을 조회하는 메소드
	 * @param productId 조회하려는 상품의 id
	 * @return 조회한 상품의 정보
	 */
	public ProductResponseDto findById(Long productId){
		return ProductResponseDto.of(getProductById(productId));
	}

	/**
	 * 상품을 삭제하는 메소드
	 * @param productId 삭제하려는 상품의 id
	 */
	public void delete(Long productId){
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
		productRepository.delete(product);
	}

	private void duplicateProduct(String name, Seller seller){
		if(productRepository.existsByNameAndSeller(name, seller)){
			throw new CustomException(ALREADY_EXISTS_PRODUCT);
		}
	}
	public void increaseProductStock(int count, Product product){
		product.increaseStock(count);
		productRepository.save(product);
	}

	public void decreaseProductStock(int count, Product product){
		product.decreaseStock(count);
		productRepository.save(product);
	}

	public void updateProductAvgScore(double avgScore, Product product){
		product.updateAvgScore(avgScore);
		productRepository.save(product);
	}

	public Product getProductById(Long id){
		return productRepository.findById(id)
			.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
	}

}
