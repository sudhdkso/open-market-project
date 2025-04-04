package com.project.openmarket.service.seller;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;

import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.dto.request.ProductUpdateReqeustDto;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.dto.request.LoginRequestDto;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.service.SellerService;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.service.ServiceTestMock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SellerServiceTest  extends ServiceTestMock {
	@InjectMocks
	private SellerService sellerService;
	@InjectMocks
	private ProductService productService;

	@Nested
	@DisplayName("판매자 등록을 할 떄")
	class signup{
		@Test
		@DisplayName("email이 null이 아니고 이미 존재하는 이메일이 아니면 판매자 등록에 성공한다.")
		void signupSeller (){
			//given
			final var request = createSeller("seller@example.com");

			given(sellerRepository.existsByEmail(anyString())).willReturn(false);
			given(sellerRepository.save(any(Seller.class))).willReturn(seller);

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
				.isInstanceOf(CustomException.class)
				.hasMessage(ALREADY_EXISTS_EMAIL.getMessage());

			//then

		}

		@DisplayName("email이 null이면 예외가 발생한다.")
		@ParameterizedTest
		@NullSource
		@ValueSource(strings = {""})
		void signupSellerEmailIsNull(String input){

			assertThatThrownBy(() -> createSeller(input))
				.isInstanceOf(CustomException.class)
				.hasMessage(INVALID_DATA_INPUT.getMessage());

		}

		@DisplayName("phoneNumber가 null이 아니지만, 잘못된 형태의 phoneNumber가 들어오면 예외가 발생한다.")
		@ParameterizedTest
		@ValueSource(strings = {"010-1234024"," ","111111111111111111111"})
		void signupSellerWithWrongPhoneNumber(String input){

			assertThatThrownBy(() -> createSeller("seller@example.com",input))
				.isInstanceOf(CustomException.class)
				.hasMessage(INVALID_DATA_INPUT.getMessage());

		}
	}

	@Nested
	@DisplayName("판매자 로그인 할 때")
	class sellerLogin{
		@Test
		@DisplayName("판매자의 email과 password가 일치하면 로그인에 성공한다.")
		void loginSuccess(){
			//given
			final var request = createLoginSeller("seller1@example.com");

			given(sellerRepository.getByEmail(anyString())).willReturn(seller);
			given(seller.isSamePassword(anyString())).willReturn(true);

			assertThatNoException().isThrownBy(() -> sellerService.login(request));


		}

		@Test
		@DisplayName("판매자의 email과 password가 일치하지 않으면 예외가 발생한다.")
		void loginByWrongPassword(){
			//given
			final var request = new LoginRequestDto("seller1@example.com","12345");
			given(sellerRepository.getByEmail(anyString())).willReturn(seller);
			given(seller.isSamePassword(any())).willReturn(false);

			assertThatThrownBy(() -> sellerService.login(request))
				.isInstanceOf(CustomException.class)
				.hasMessage(NOT_MATCH_PASSWORD.getMessage());

		}

		@Test
		@DisplayName("판매자가 존재하지 않는 email로 로그인 시 예외가 발생한다.")
		void loginByNotFoundEmail(){
			//given
			final var request = new LoginRequestDto("test@example.com","1234");

			//when
			doThrow(new CustomException(NOT_FOUND_USER)).when(sellerRepository).getByEmail(any());
			//then
			assertThatThrownBy(() -> sellerService.login(request))
				.isInstanceOf(CustomException.class)
				.hasMessage(NOT_FOUND_USER.getMessage());
		}
	}


	@Test
	@DisplayName("올바른 판매자 id로 판매자를 요청하면 성공한다.")
	void findSellerByValidId(){

		when(sellerRepository.getById(any())).thenReturn(seller);

		ObjectId id = new ObjectId("67ea40df5aeb2844f05b84e8");
		assertThatNoException()
			.isThrownBy(() -> sellerService.findById(id));

		then(sellerRepository)
			.should(times(1))
			.getById(any());
	}

	@Test
	@DisplayName("판매자에게 없는 상품 이름과 판매자가 존재하면 상품 등록에 성공한다.")
	void createProductByNameAndSeller(){
		final var request = createProduct("상품");
		Product savedProduct = spy(Product.of(request, seller));
		given(productRepository.save(any(Product.class))).willReturn(savedProduct);
		given(productRepository.existsByNameAndSeller(anyString(), any(Seller.class))).willReturn(false);

		ObjectId id = new ObjectId("67ea40df5aeb2844f05b84e8");
		given(savedProduct.getId()).willReturn(id);

		assertThatNoException()
			.isThrownBy(() -> productService.create(request, seller));

		then(productRepository)
			.should(times(1))
			.save(any(Product.class));
	}

	@Test
	@DisplayName("판매자에게 없는 상품 이름과 판매자가 존재하면 상품 업데이트에 성공한다.")
	void updateProductByNameAndSeller(){
		final var request = updateProduct("일품", 900);

		given(productRepository.findByIdWithLock(any())).willReturn(Optional.of(product));
		given(product.isSameName(anyString())).willReturn(false);
		given(productRepository.existsByNameAndSeller(anyString(), any(Seller.class))).willReturn(false);

		ObjectId id = new ObjectId("67ea40df5aeb2844f05b84e8");
		given(product.getId()).willReturn(id);

		assertThatNoException()
			.isThrownBy(() -> productService.update(id.toHexString(), request, seller));

		then(product)
			.should(times(1))
			.update(request);
	}

	@Test
	@DisplayName("증가시킬 금액과 판매자가 들어오면 성공한다.")
	void SuccessProcessPaymentTest(){

		doNothing().when(seller).increaseCash(anyLong());

		assertThatNoException()
			.isThrownBy(() -> sellerService.processPayment(anyLong(), seller));

		then(sellerRepository)
			.should(times(1))
			.save(any(Seller.class));
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

	ProductUpdateReqeustDto updateProduct(String name, int price){
		return new ProductUpdateReqeustDto(name, price, 10);
	}
}
