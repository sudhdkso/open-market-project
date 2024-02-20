package com.project.openmarket.consmer;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.dto.request.ConsumerLoginRequestDto;
import com.project.openmarket.domain.user.service.ConsumerService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConsumerServiceTest {
	@Autowired
	private ConsumerService consumerService;

	@Test
	@Order(1)
	@DisplayName("고객등록_테스트")
	void signupConsumer(){
		//given
		final var request = ConsumerStep.고객생성요청_생성();

 		//when
		final var response = consumerService.saveConumser(request);

		//then
		assertThat(request.email())
			.isEqualTo(response.email());
	}

	@Test
	@Order(2)
	@DisplayName("고객등록_이메일중복_테스트")
	void sinupByDuplicatedEmail(){
		final var request = new ConsumerCreateReqestDto("asdf@example.com","dd","010-0000-0000","1234","");

		// when
		Throwable thrown = catchThrowable(() -> consumerService.saveConumser(request));

		//then
		assertThat(thrown)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@Order(2)
	@DisplayName("고객등록_이메일_없이_테스트")
	void sinupByEmptyEmail(){
		//given
		String email = "";
		// when
		Throwable thrown = catchThrowable(() -> new ConsumerCreateReqestDto(email,"dd","010-0000-0000","1234",""));

		//then
		assertThat(thrown)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@Order(2)
	@DisplayName("고객등록_모두_빈값_테스트")
	void sinupByAllEmptyInfo(){
		// when
		Throwable thrown = catchThrowable(() -> new ConsumerCreateReqestDto("","","","",""));

		//then
		assertThat(thrown)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@Order(3)
	@DisplayName("고객_로그인_성공_테스트")
	void consumerLogin(){
		//given
		final var request = ConsumerStep.로그인요청_생성();

		//when
		final var response = consumerService.login(request);

		//then
		assertThat(response.email())
			.isEqualTo(request.email());
	}

	@Test
	@Order(4)
	@DisplayName("고객_없는_이메일_로그인_실패_테스트")
	void loginByWrongEmail(){
		//given
		final var request = new ConsumerLoginRequestDto("test@example.com", "1234");

		// when
		Throwable thrown = catchThrowable(() -> consumerService.login(request));

		//then
		assertThat(thrown)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("고객_로그인_틀린_패스워드_테스트")
	void loginByWrongPassword(){
		//given
		final var request = new ConsumerLoginRequestDto("asdf3@example.com", "12345");

		// when
		Throwable thrown = catchThrowable(() -> consumerService.login(request));

		//then
		assertThat(thrown)
			.isInstanceOf(IllegalArgumentException.class);

	}
}
