package com.project.openmarket.consmer;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.dto.request.ConsumerLoginRequestDto;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.repository.ConsumerRepository;
import com.project.openmarket.domain.user.service.ConsumerService;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConsumerServiceTest {
	@InjectMocks
	private ConsumerService consumerService;
	@Mock
	private ConsumerRepository consumerRepository;
	@Test
	@Order(1)
	@DisplayName("고객등록_테스트")
	void signupConsumer(){
		//given
		final var request = createConsumer();
 		//when
		when(consumerRepository.save(any(Consumer.class))).thenReturn(request.toEntity());
		final var response = consumerService.saveConumser(request);

		//then
		assertThat(request.email())
			.isEqualTo(response.email());
	}

	@Test
	@Order(2)
	@DisplayName("고객등록_이메일중복_테스트")
	void sinupByDuplicatedEmail(){
		final var request = new ConsumerCreateReqestDto("asdf3@example.com","dd","010-0000-0000","1234","");

		// when
		when(consumerRepository.existsByEmail(request.email())).thenReturn(true);
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
		final var request = createLogin();

		//when
		when(consumerRepository.findByEmail(request.email())).thenReturn(Optional.of(createConsumer().toEntity()));
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
		when(consumerRepository.findByEmail(request.email())).thenReturn(Optional.empty());
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
	
	private ConsumerCreateReqestDto createConsumer(){
		String email = "asdf3@example.com";
		String name = "김하얀";
		String phoneNumber = "010-0000-0000";
		String password = "1234";
		String address = "어디지";
		
		return new ConsumerCreateReqestDto(email, name, phoneNumber, password, address);
	}

	private ConsumerLoginRequestDto createLogin(){
		String email = "asdf3@example.com";
		String password = "1234";

		return new ConsumerLoginRequestDto(email, password);
	}
}
