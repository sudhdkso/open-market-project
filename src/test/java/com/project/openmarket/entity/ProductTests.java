package com.project.openmarket.entity;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.dto.request.ProductUpdateReqeustDto;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Seller;

class ProductTests {
	@Mock
	Seller seller;

	@DisplayName("dto를 이용하여 상품이 성공적으로 생성된다.")
	@Test
	void whenCreateWithDto_thenSuccess() {
		var request = createProduct("상품테스트");

		assertThatNoException()
			.isThrownBy(() -> Product.of(request, seller));
	}

	@ParameterizedTest
	@DisplayName("구매하려는 상품의 수량이 재고보다 적거나 같으면 true를 반환한다.")
	@ValueSource(ints = {1, 2, 5, 10})
	void whenQuantityIsEnough_thenReturnTrue(int count) {
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
	@ValueSource(ints = {11, 13, 190})
	void whenQuantityExceedsStock_thenReturnFalse(int count) {
		//given
		var request = createProduct("상품테스트");
		Product product = Product.of(request, seller);
		//when
		boolean result = product.canBuy(count);
		//then
		assertThat(result).isFalse();
	}

	@DisplayName("상품의 메서드를 통해서 재고를 감소시킬 수 있다.")
	@Test
	void whenStockGiven_thenDecreaseStock() {
		//given
		var request = createProduct("상품테스트");
		Product product = Product.of(request, seller);
		int count = 5;
		int expected = product.getStock() - count;
		//when
		product.decreaseStock(5);
		//then
		assertThat(product.getStock()).isEqualTo(expected);
	}

	@DisplayName("상품의 메서드를 통해서 재고를 증가시킬 수 있다.")
	@Test
	void whenStockGiven_thenIncreaseStock() {
		//given
		var request = createProduct("상품테스트");
		Product product = Product.of(request, seller);
		int count = 5;
		int expected = product.getStock() + count;
		//when
		product.increaseStock(count);
		//then
		assertThat(product.getStock()).isEqualTo(expected);
	}

	@DisplayName("유효한 상품 정보가 주어지면 상품이 성공적으로 업데이트된다.")
	@Test
	void whenProductInfoIsValid_thenUpdateProductSuccess() {
		//given
		Product product = new Product("Previous Name", "", 1000, 5, seller);
		ProductUpdateReqeustDto request = new ProductUpdateReqeustDto("New Name", "", 5000, 5);

		//when
		product.update(request);

		//then
		assertThat(product.getName()).isEqualTo(request.name());
		assertThat(product.getPrice()).isEqualTo(request.price());
		assertThat(product.getStock()).isEqualTo(request.stock());
	}

	@DisplayName("평점이 주어지면 평균 평점이 수정된다.")
	@Test
	void whenAvgScoreGiven_thenUpdateAvgRating() {
		//given
		Product product = new Product("product1", "description", 1000, 1, seller);
		double newAvgScore = 4.0;
		//when
		product.updateAvgRating(newAvgScore);
		//then
		assertThat(product.getAvgRating()).isEqualTo(newAvgScore);
	}

	@DisplayName("상품 이름이 동일하면 true를 반환한다.")
	@Test
	void whenProductNameIsSame_thenReturnTrue() {
		//given
		Product product = new Product("product1", "description", 1000, 1, seller);
		String another = "product1";
		//when
		boolean result = product.isSameName(another);
		//then
		assertThat(result).isTrue();
	}

	@DisplayName("상품 이름이 동일하지 않으면 false를 반환한다.")
	@Test
	void whenProductNameIsNotSame_thenReturnFalse() {
		//given
		Product product = new Product("product1", "description", 1000, 1, seller);
		String another = "product2";
		//when
		boolean result = product.isSameName(another);
		//then
		assertThat(result).isFalse();
	}

	ProductRequestDto createProduct(String name) {
		return new ProductRequestDto(name, "", 1000, 10);
	}
}
