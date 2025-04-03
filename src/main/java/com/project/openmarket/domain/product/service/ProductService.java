package com.project.openmarket.domain.product.service;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.dto.request.ProductUpdateReqeustDto;
import com.project.openmarket.domain.product.dto.response.ProductCreateResponseDto;
import com.project.openmarket.domain.product.dto.response.ProductListResponsesDto;
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
	public ProductCreateResponseDto create(ProductRequestDto request, Seller seller){
		//상품 이름과 판매자로 일치하는 상품이 있는 경우 예외 발생
		duplicateProduct(request.name(), seller);
		Product product = productRepository.save(Product.of(request, seller));
		return ProductCreateResponseDto.of(product, seller);
	}

	/**
	 * 상품을 업데이트하는 메소드
	 * @param request 업데이트할 상품의 정보
	 * @param seller 상품을 업데이터하려는 판매자
	 * @return 업데이트된 상품의 정보
	 */
	@Transactional
	public ProductResponseDto update(ProductUpdateReqeustDto request, Seller seller){
		Product product = productRepository.findByIdWithLock(request.id())
			.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
		//기존 상품의 이름과 수정하려는 상품의 이름이 다른 경우에만 확인
		if( request.name() != null && !product.isSameName(request.name())){
			duplicateProduct(request.name(), seller);
		}
		product.update(request);

		productRepository.save(product);

		return ProductResponseDto.of(product);
	}

	/**
	 * 상품을 조회하는 메소드
	 * @param productId 조회하려는 상품의 id
	 * @return 조회한 상품의 정보
	 */
	public ProductResponseDto findById(String productId){
		System.out.println(productId);
		Product product = getProductById(new ObjectId(productId));
		return ProductResponseDto.of(product);
	}
	
	public List<Product> findByScoreDesc(String name){
		return productRepository.findByNameContainsOrderByAvgScoreDesc(name);
	}

	public List<ProductResponseDto> findProductByName(String name, Pageable pageable){
		return productRepository.findByName(name)
			.stream().map(ProductResponseDto::of)
			.toList();
	}

	public ProductListResponsesDto findAllProduct(){
		List<Product> products = productRepository.findAll();
		return ProductListResponsesDto.of(products);
	}

	/**
	 * 상품을 삭제하는 메소드
	 * @param productId 삭제하려는 상품의 id
	 */
	public void delete(Long productId){
		Product product = productRepository.findByIdWithLock(productId)
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

	public Product getProductById(ObjectId id){
		return productRepository.getById(id);
	}



}
