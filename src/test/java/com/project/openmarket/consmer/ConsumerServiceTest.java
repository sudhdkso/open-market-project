package com.project.openmarket.consmer;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.openmarket.domain.user.dto.reposne.UserCreateResponseDto;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.service.ConsumerService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConsumerServiceTest {
	@Autowired
	private ConsumerService consumerService;

	@Test
	@Order(1)
	@DisplayName("고객등록_테스트")
	void 고객_등록_테스트(){
		//given
		ConsumerCreateReqestDto request = new ConsumerCreateReqestDto("asdf3@example.com","dd","010-0000-0000","1234","");

		//when
		UserCreateResponseDto response = consumerService.saveConumser(request);

		//then
		assertThat(request.email())
			.isEqualTo(response.email());
	}

	@Test
	@Order(2)
	@DisplayName("고객등록_이메일중복_테스트")
	void 이메일_중복_테스트(){
		ConsumerCreateReqestDto request = new ConsumerCreateReqestDto("asdf3@example.com","dd","010-0000-0000","1234","");

		// when
		Throwable thrown = catchThrowable(() -> consumerService.saveConumser(request));

		//then
		assertThat(thrown)
			.isInstanceOf(IllegalArgumentException.class);

	}
}
