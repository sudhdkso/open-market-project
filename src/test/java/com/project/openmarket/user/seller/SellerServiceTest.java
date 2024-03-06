package com.project.openmarket.user.seller;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;

import com.project.openmarket.domain.user.dto.request.LoginRequestDto;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.service.SellerService;
import com.project.openmarket.user.ServiceTestMock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SellerServiceTest  extends ServiceTestMock {
	@InjectMocks
	private SellerService sellerService;

	@BeforeEach
	void setUp() {
		seller = createSeller("seller@example.com").toEntity();
	}

	@Test
	@Order(1)
	@DisplayName("판매자 등록을 할 때 email이 null이 아니고 이미 존재하는 이메일이 아니면 판매자 등록에 성공한다.")
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
	@DisplayName("판매자 등록을 할 때 email이 null이 아니지만, 이미 존재하는 이메일일 경우 예외가 발생한다.")
	void signupSellerEmailIsDuplicated (){
		//given
		final var request = createSeller("seller@example.com");

		given(sellerRepository.existsByEmail(anyString())).willReturn(true);

		//when
		assertThatThrownBy(() -> sellerService.save(request))
			.isInstanceOf(IllegalArgumentException.class);

		//then

	}


	@DisplayName("판매자 등록 할 때 email이 null이면 예외가 발생한다.")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {""})
	void signupSellerEmailIsNull(String input){

		assertThatThrownBy(() -> createSeller(input))
			.isInstanceOf(IllegalArgumentException.class);

	}

	@Test
	@Order(2)
	@DisplayName("판매자의 email과 password가 일치하면 로그인에 성공한다.")
	void sellerLogin(){
		//given
		final var request = createLoginSeller("seller@example.com");

		given(sellerRepository.findByEmail(anyString())).willReturn(Optional.of(seller));

		assertThatNoException().isThrownBy(() -> sellerService.login(request));


	}

	@Test
	@Order(2)
	@DisplayName("판매자의 email과 password가 일치하지 않으면 예외가 발생한다.")
	void loginByWrongPassword(){
		//given
		final var request = new LoginRequestDto("seller@example.com","12345");
		given(sellerRepository.findByEmail(anyString())).willReturn(Optional.of(seller));

		assertThatThrownBy(() -> sellerService.login(request))
			.isInstanceOf(IllegalArgumentException.class);

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
			.isInstanceOf(IllegalArgumentException.class);
	}
	SellerCreateRequestDto createSeller(String email){
		String name = "판매자";
		String phoneNumber = "010-0000-0000";
		String password = "1234";
		return new SellerCreateRequestDto(email, name, phoneNumber, password);
	}

	LoginRequestDto createLoginSeller(String email){
		String password = "1234";
		return new LoginRequestDto(email, password);
	}
}
