package com.project.openmarket.entity;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Seller;

class ProductTests {
	@Mock
	Seller seller;

	@Test
	@DisplayName("dto를 이용하여 상품이 성공적으로 생성된다.")
	void successCreateProduct(){
		var request = createProduct("상품테스트");

		assertThatNoException()
			.isThrownBy(() -> Product.of(request, seller));
	}

	@ParameterizedTest
	@DisplayName("구매하려는 상품의 수량이 재고보다 적거나 같으면 true를 반환한다.")
	@ValueSource(ints = {1,2,5,10})
	void enoughStockReturnTrue(int count){
		//given
		var request = createProduct("상품테스트");
		Product product = Product.of(request, seller);
		//when
		boolean result = product.canBuy(count);
		//then
		assertThat(result).isTrue();
	}

	@ParameterizedTest
	@DisplayName("구매하려는 상품의 수량이 재고보다 많으면 false를 반환한다.")
	@ValueSource(ints = {11,13,190})
	void notEnoughStockReturnFalse(int count){
		//given
		var request = createProduct("상품테스트");
		Product product = Product.of(request, seller);
		//when
		boolean result = product.canBuy(count);
		//then
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("상품의 메서드를 통해서 재고를 감소시킬 수 있다.")
	void successDecreaseStock(){
		//given
		var request = createProduct("상품테스트");
		Product product = Product.of(request, seller);
		int count = 5;
		int expected = product.getStock()-count;
		//when
		product.decreaseStock(5);
		//then
		assertThat(product.getStock()).isEqualTo(expected);
	}

	@Test
	@DisplayName("상품의 메서드를 통해서 재고를 증가시킬 수 있다.")
	void successIncreaseStock(){
		//given
		var request = createProduct("상품테스트");
		Product product = Product.of(request, seller);
		int count = 5;
		int expected = product.getStock()+count;
		//when
		product.increaseStock(count);
		//then
		assertThat(product.getStock()).isEqualTo(expected);
	}

	ProductRequestDto createProduct(String name){
		return new ProductRequestDto(name, 1000, 10);
	}
}
