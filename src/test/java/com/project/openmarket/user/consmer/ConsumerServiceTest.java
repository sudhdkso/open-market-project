package com.project.openmarket.user.consmer;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;

import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.dto.request.LoginRequestDto;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.user.ServiceTestMock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConsumerServiceTest extends ServiceTestMock {
	@InjectMocks
	private ConsumerService consumerService;

	@Test
	@Order(1)
	@DisplayName("고객 등록에서 등록에 성공할 경우 request와 response가 같은 email을 반환한다.")
	void signupConsumer(){
		//given
		final var request = createConsumer();

		//when
		given(consumerRepository.existsByEmail(anyString())).willReturn(false);

		when(consumerRepository.save(any(Consumer.class))).thenReturn(request.toEntity());
		final var response = consumerService.signup(request);

		//then
		assertThat(request.email())
			.isEqualTo(response.email());
	}

	@Test
	@Order(2)
	@DisplayName("고객 등록에서 이미 고객이 있는 이메일로 등록을 요청할 경우 예외가 발생한다.")
	void sinupByDuplicatedEmail(){
		final var request = new ConsumerCreateReqestDto("asdf3@example.com","dd","010-0000-0000","1234","");

		given(consumerRepository.existsByEmail(anyString())).willReturn(true);

		assertThatThrownBy(() -> consumerService.signup(request))
			.isInstanceOf(IllegalArgumentException.class);


	}

	@Test
	@Order(2)
	@DisplayName("고객 등록에서 이메일을 빈값으로 등록 요청할 경우 예외가 발생한다.")
	void sinupByEmptyEmail(){
		//given
		String email = "";

		assertThatThrownBy(() -> new ConsumerCreateReqestDto(email,"dd","010-0000-0000","1234",""))
			.isInstanceOf(IllegalArgumentException.class);

	}

	@Test
	@Order(2)
	@DisplayName("고객 등록에서 모두 빈값으로 등록을 요청할 경우 예외가 발생한다.")
	void sinupByAllEmptyInfo(){
		// when
		Throwable thrown = catchThrowable(() -> new ConsumerCreateReqestDto("","","","",""));

		//then
		assertThat(thrown)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@Order(3)
	@DisplayName("고객 로그인이 성공할 경우 request와 resopnse이 같은 이메일을 반환한다.")
	void consumerLogin(){
		//given
		final var request = createLogin();

		//when
		given(consumerRepository.findByEmail(request.email())).willReturn((Optional.of(createConsumer().toEntity())));
		final var response = consumerService.login(request);

		//then
		assertThat(response.email())
			.isEqualTo(request.email());
	}

	@Test
	@Order(4)
	@DisplayName("고객 로그인에서 없는 이메일로 로그인 할 경우 예외가 발생한다.")
	void loginByInvalidEmail(){
		//given
		final var request = new LoginRequestDto("test@example.com", "1234");

		// when
		given(consumerRepository.findByEmail(anyString())).willReturn(Optional.empty());

		//then
		assertThatThrownBy(() -> consumerService.login(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@Order(4)
	@DisplayName("고객 로그인에서 틀린 비밀번호로 로그인 할 경우 예외가 발생한다.")
	void loginByWrongPassword(){
		//given
		final var request = new LoginRequestDto("asdf3@example.com", "12345");

		// when
		given(consumerRepository.findByEmail(anyString())).willReturn(Optional.of(createConsumer().toEntity()));

		//then
		assertThatThrownBy(() -> consumerService.login(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	private ConsumerCreateReqestDto createConsumer(){
		String email = "asdf3@example.com";
		String name = "김하얀";
		String phoneNumber = "010-0000-0000";
		String password = "1234";
		String address = "어디지";
		
		return new ConsumerCreateReqestDto(email, name, phoneNumber, password, address);
	}

	private LoginRequestDto createLogin(){
		String email = "asdf3@example.com";
		String password = "1234";

		return new LoginRequestDto(email, password);
	}
}