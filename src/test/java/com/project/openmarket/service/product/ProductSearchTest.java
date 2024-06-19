package com.project.openmarket.service.product;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.project.openmarket.domain.product.dto.response.ProductResponseDto;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.repository.ProductRepository;
import com.project.openmarket.domain.product.service.ProductService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProductSearchTest {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductService productService;

	@Test
	@DisplayName("물건의 이름을 검색할 때 리뷰점수의 오름차순으로 검색할 수 있다.")
	void findProductByAvgScore(){
	    //given
		String name = "a";

	    //when
		List<Product> productList = productService.findByScoreDesc(name);

	    //then
		double prvScore = 5.0;
		for (Product product : productList) {
			assertTrue(product.getAvgScore() <= prvScore);
			prvScore = product.getAvgScore();
		}
		//3sec 222ms
	}

	@Test
	@DisplayName("물건의 이름을 검색할 때 페이징을 이용하여 리뷰점수의 오름차순으로 검색할 수 있다.")
	void findProductByAvgScoreWithPageable(){
		//given
		String name = "a";
		Pageable pageable = PageRequest.of(0,10, Sort.by("avgScore").descending());
		//when
		List<ProductResponseDto> productList = productService.findProductByName(name, pageable);
		//then
		double prvScore = 5.0;
		for (ProductResponseDto product : productList) {
			assertTrue(product.avgScore() <= prvScore);
			prvScore = product.avgScore();
		}
		//2sec 579ms
		//아마 스프링 부트 동작 시간이 한 2sec 정도
	}
}
