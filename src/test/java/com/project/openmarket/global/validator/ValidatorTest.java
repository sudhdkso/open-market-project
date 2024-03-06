package com.project.openmarket.global.validator;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ValidatorTest {
	@ParameterizedTest
	@ValueSource(strings = {"asdf3@example","asd","@example.com"})
	@DisplayName("이메일_형식_테스트")
	void wrongEmailPattern(String input){
		//given

		//when
		boolean result = Validator.validateEmail(input);
		//then
		assertThat(result)
			.isFalse();
	}

	@ParameterizedTest
	@ValueSource(strings = {"010-1234-56","01000000000","010","010-23-","xcvd"})
	@DisplayName("틀린_휴대전화_형식_테스트")
	void wrongPhoneNumberPattern(String input){
		//given

		// when
		boolean result = Validator.validatePhoneNumber(input);
		//then
		assertThat(result)
			.isFalse();
	}
}
