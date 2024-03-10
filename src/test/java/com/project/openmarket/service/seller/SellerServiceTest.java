package com.project.openmarket.service.seller;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;

import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.dto.request.LoginRequestDto;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.service.SellerService;
import com.project.openmarket.service.ServiceTestMock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SellerServiceTest  extends ServiceTestMock {
	@InjectMocks
	private SellerService sellerService;
	@InjectMocks
	private ProductService productService;

	@BeforeEach
	void setUp() {
		seller = createSeller("seller1@example.com").toEntity();
	}

	@Nested
	@DisplayName("판매자 등록을 할 떄")
	class signup{
		@Test
		@Order(1)
		@DisplayName("email이 null이 아니고 이미 존재하는 이메일이 아니면 판매자 등록에 성공한다.")
		void signupSeller (){
			//given
			final var request = createSeller("seller@example.com");

			given(sellerRepository.existsByEmail(anyString())).willReturn(false);
			given(sellerRepository.save(any(Seller.class))).willReturn(request.toEntity());

			//when
			assertThatNoException().isThrownBy(() -> sellerService.save(request));

			//then
			then(sellerRepository)
				.should(times(1))
				.save(any(Seller.class));
		}

		@Test
		@DisplayName("email이 null이 아니지만, 이미 존재하는 이메일일 경우 예외가 발생한다.")
		void signupSellerEmailIsDuplicated (){
			//given
			final var request = createSeller("seller1@example.com");

			given(sellerRepository.existsByEmail(anyString())).willReturn(true);

			//when
			assertThatThrownBy(() -> sellerService.save(request))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(ALREADY_EXISTS_EMAIL.getMessage());

			//then

		}

		@DisplayName("email이 null이면 예외가 발생한다.")
		@ParameterizedTest
		@NullSource
		@ValueSource(strings = {""})
		void signupSellerEmailIsNull(String input){

			assertThatThrownBy(() -> createSeller(input))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(INVALID_DATA_INPUT.getMessage());

		}

		@DisplayName("phoneNumber가 null이 아니지만, 잘못된 형태의 phoneNumber가 들어오면 예외가 발생한다.")
		@ParameterizedTest
		@ValueSource(strings = {"010-1234024"," ","111111111111111111111"})
		void signupSellerWithWrongPhoneNumber(String input){

			assertThatThrownBy(() -> createSeller("seller@example.com",input))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(INVALID_DATA_INPUT.getMessage());

		}
	}

	@Nested
	@DisplayName("판매자 로그인 할 때")
	class sellerLogin{
		@Test
		@Order(2)
		@DisplayName("판매자의 email과 password가 일치하면 로그인에 성공한다.")
		void loginSuccess(){
			//given
			final var request = createLoginSeller("seller1@example.com");

			given(sellerRepository.findByEmail(anyString())).willReturn(Optional.of(seller));

			assertThatNoException().isThrownBy(() -> sellerService.login(request));


		}

		@Test
		@Order(2)
		@DisplayName("판매자의 email과 password가 일치하지 않으면 예외가 발생한다.")
		void loginByWrongPassword(){
			//given
			final var request = new LoginRequestDto("seller1@example.com","12345");
			given(sellerRepository.findByEmail(anyString())).willReturn(Optional.of(seller));

			assertThatThrownBy(() -> sellerService.login(request))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_MATCH_PASSWORD.getMessage());

		}

		@Test
		@DisplayName("판매자가 존재하지 않는 email로 로그인 시 예외가 발생한다.")
		void loginByNotFoundEmail(){
			//given
			final var request = new LoginRequestDto("test@example.com","1234");

			//when
			given(sellerRepository.findByEmail(anyString())).willReturn(Optional.empty());

			//then
			assertThatThrownBy(() -> sellerService.login(request))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_FOUND_USER.getMessage());
		}
	}

	@Test
	@DisplayName("올바르지 않은 판매자 id로 판매자를 요청하면 예외가 발생한다.")
	void findSellerByInvalidId(){
		given(sellerRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		assertThatThrownBy(() -> sellerService.findById(1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(NOT_FOUND_USER.getMessage());
	}

	@Test
	@DisplayName("올바른 판매자 id로 판매자를 요청하면 성공한다.")
	void findSellerByValidId(){
		given(sellerRepository.findById(anyLong()))
			.willReturn(Optional.of(seller));

		assertThatNoException()
			.isThrownBy(() -> sellerService.findById(1L));

		then(sellerRepository)
			.should(times(1))
			.findById(anyLong());

	}

	@Test
	@DisplayName("판매자에게 없는 상품 이름과 판매자가 존재하면 상품 등록에 성공한다.")
	void createProductByNameAndSeller(){
		final var request = createProduct("상품");

		given(productRepository.save(any(Product.class))).willReturn(Product.of(request, seller));
		given(productRepository.existsByNameAndSeller(anyString(), any(Seller.class))).willReturn(false);

		assertThatNoException()
			.isThrownBy(() -> productService.create(request, seller));

		then(productRepository)
			.should(times(1))
			.save(any(Product.class));
	}

	@Test
	@DisplayName("판매자에게 없는 상품 이름과 판매자가 존재하면 상품 등록에 성공한다.")
	void updateProductByNameAndSeller(){
		final var request = createProduct("상품");

		given(productRepository.save(any(Product.class))).willReturn(Product.of(request, seller));
		given(productRepository.existsByNameAndSeller(anyString(), any(Seller.class))).willReturn(false);

		assertThatNoException()
			.isThrownBy(() -> productService.create(request, seller));

		then(productRepository)
			.should(times(1))
			.save(any(Product.class));
	}

	SellerCreateRequestDto createSeller(String email){
		String name = "판매자";
		String phoneNumber = "010-0000-0000";
		String password = "1234";
		return new SellerCreateRequestDto(email, name, phoneNumber, password);
	}

	SellerCreateRequestDto createSeller(String email, String phoneNumber){
		String name = "판매자";
		String password = "1234";
		return new SellerCreateRequestDto(email, name, phoneNumber, password);
	}

	LoginRequestDto createLoginSeller(String email){
		String password = "1234";
		return new LoginRequestDto(email, password);
	}

	ProductRequestDto createProduct(String name){
		return new ProductRequestDto(name, 1000, 10);
	}
}
