package com.project.openmarket.consmer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.project.openmarket.ApiTest;

class ConsumerApiTest extends ApiTest {

	@Test
	@DisplayName("고객등록요청_api_테스트")
	void signupApi(){
		final var response = ConsumerStep.고객생성요청(ConsumerStep.고객생성요청_생성());
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("로그인_api_테스트")
	void loginApi(){

		ConsumerStep.고객생성요청(ConsumerStep.고객생성요청_생성());

		final var response = ConsumerStep.로그인요청(ConsumerStep.로그인요청_생성());
	    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

	}

}
