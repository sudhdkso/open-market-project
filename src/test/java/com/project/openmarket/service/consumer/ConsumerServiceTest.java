package com.project.openmarket.service.consumer;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;

import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.dto.request.LoginRequestDto;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.service.ServiceTestMock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConsumerServiceTest extends ServiceTestMock {
	@InjectMocks
	private ConsumerService consumerService;

	@Test
	@Order(1)
	@DisplayName("고객 등록에서 등록에 성공할 경우 request와 response가 같은 email을 반환한다.")
	void signupConsumer(){
		//given
		final var request = createConsumer("consumer@example.com");

		//when
		given(consumerRepository.existsByEmail(anyString())).willReturn(false);
		given(consumerRepository.save(any(Consumer.class))).willReturn(consumer);

		assertThatNoException().isThrownBy(() -> consumerService.save(request));
		//then
		then(consumerRepository)
			.should(times(1))
			.save(any(Consumer.class));
	}

	@Test
	@DisplayName("고객 등록에서 이미 고객이 있는 이메일로 등록을 요청할 경우 예외가 발생한다.")
	void sinupByDuplicatedEmail(){
		final var request = new ConsumerCreateReqestDto("consumer1@example.com","dd","010-0000-0000","1234","");

		given(consumerRepository.existsByEmail(anyString())).willReturn(true);

		assertThatThrownBy(() -> consumerService.save(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ALREADY_EXISTS_EMAIL.getMessage());


	}

	@ParameterizedTest
	@DisplayName("고객 등록에서 이메일을 빈값으로 등록 요청할 경우 예외가 발생한다.")
	@NullSource
	@ValueSource(strings = {""})
	void sinupByEmptyEmail(String email){

		assertThatThrownBy(() -> new ConsumerCreateReqestDto(email,"dd","010-0000-0000","1234",""))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(INVALID_DATA_INPUT.getMessage());

	}

	@DisplayName("고객 등록 할 때 phoneNumber가 null이 아니지만, 잘못된 형태의 phoneNumber가 들어오면 예외가 발생한다.")
	@ParameterizedTest
	@ValueSource(strings = {"010-1234024"," ","2234023-2321"})
	void signupConsumerWithWrongPhoneNumber(String input){

		assertThatThrownBy(() -> createConsumer("consumer@example.com",input))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(INVALID_DATA_INPUT.getMessage());

	}

	@Test
	@Order(2)
	@DisplayName("고객의 email과 password가 일치하면 로그인에 성공한다.")
	void consumerLogin(){
		//given
		final var request = createLogin("consumer@example.com");

		//when
		given(consumerRepository.findByEmail(anyString())).willReturn((Optional.of(consumer)));
		given(consumer.isSamePassword(anyString())).willReturn(true);

		//then
		assertThatNoException().isThrownBy(() -> consumerService.login(request));
	}

	@Test
	@DisplayName("고객 로그인에서 없는 이메일로 로그인 할 경우 예외가 발생한다.")
	void loginByInvalidEmail(){
		//given
		final var request = new LoginRequestDto("test@example.com", "1234");

		// when
		given(consumerRepository.findByEmail(anyString())).willReturn(Optional.empty());

		//then
		assertThatThrownBy(() -> consumerService.login(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(NOT_FOUND_USER.getMessage());
	}

	@Test
	@Order(2)
	@DisplayName("고객 로그인에서 틀린 비밀번호로 로그인 할 경우 예외가 발생한다.")
	void loginByWrongPassword(){
		//given
		final var request = new LoginRequestDto("consumer@example.com", "12345");

		// when
		given(consumerRepository.findByEmail(anyString())).willReturn(Optional.of(consumer));
		given(consumer.isSamePassword(anyString())).willReturn(false);

		//then
		assertThatThrownBy(() -> consumerService.login(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(NOT_MATCH_PASSWORD.getMessage());
	}

	@Test
	@DisplayName("고객의 id가 존재하는 id이면 고객을 찾을 수 있다.")
	void successGetConsumerByValidId(){

		given(consumerRepository.findById(anyLong())).willReturn(Optional.of(consumer));

		assertThatNoException()
			.isThrownBy(() -> consumerService.getConsumerById(1L));
	}

	@Test
	@DisplayName("고객의 id가 존재하지 않는 id이면 고객을 찾을 수 없다.")
	void failGetConsumerByInvalidId(){

		given(consumerRepository.findById(anyLong())).willReturn(Optional.empty());

		assertThatThrownBy(() -> consumerService.getConsumerById(1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(NOT_FOUND_USER.getMessage());
	}

	@Test
	@DisplayName("Amount클래스를 매개변수로 받아 고객의 Cache와 Point를 증가시키는데 성공한다.")
	void increaseAmountTest(){
		Amount amount = new Amount(1000L,100L);

		doNothing().when(consumer).increaseAmount(any(Amount.class));

		assertThatNoException()
			.isThrownBy(() -> consumerService.increaseAmount(amount, consumer));

		then(consumerRepository)
			.should(times(1))
			.save(any(Consumer.class));
	}

	@Test
	@DisplayName("Amount클래스를 매개변수로 받아 고객의 Cache와 Point를 감소시키는데 성공한다.")
	void decreaseAmountTest(){
		Amount amount = new Amount(1000L,100L);

		doNothing().when(consumer).decreaseAmount(any(Amount.class));

		assertThatNoException()
			.isThrownBy(() -> consumerService.decreaseAmount(amount, consumer));

		then(consumerRepository)
			.should(times(1))
			.save(any(Consumer.class));
	}

	@Test
	@DisplayName("증가 시킬 포인트와 고객이 들어오면 성공한다")
	void processPointsTest(){
		doNothing().when(consumer).increasePoint(anyLong());

		assertThatNoException()
			.isThrownBy(() -> consumerService.processPoints(1000L, consumer));

		then(consumerRepository)
			.should(times(1))
			.save(any(Consumer.class));

	}

	ConsumerCreateReqestDto createConsumer(String email){
		String name = "김하얀";
		String phoneNumber = "010-0000-0000";
		String password = "1234";
		String address = "어디지";
		
		return new ConsumerCreateReqestDto(email, name, phoneNumber, password, address);
	}

	ConsumerCreateReqestDto createConsumer(String email, String phoneNumber){
		String name = "판매자";
		String password = "1234";
		String address = "어디지";
		return new ConsumerCreateReqestDto(email, name, phoneNumber, password, address);
	}


	LoginRequestDto createLogin(String email){
		String password = "1234";
		return new LoginRequestDto(email, password);
	}
}
