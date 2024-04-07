package com.project.openmarket.global.validator;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ValidatorTest {
	@ParameterizedTest
	@ValueSource(strings = {"asdf3@example","asd","@example.com"})
	@DisplayName("이메일 형식 테스트")
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
	@DisplayName("틀린 휴대전화 형식 테스트")
	void wrongPhoneNumberPattern(String input){
		//given

		// when
		boolean result = Validator.validatePhoneNumber(input);
		//then
		assertThat(result)
			.isFalse();
	}

	@ParameterizedTest
	@ValueSource(ints = {-1,-10,-100001})
	@DisplayName("숫자가 음수가 입력되면 false를 반환한다.")
	void negativeNumberTest(int input){
		//given

		// when
		boolean result = NumberValidator.isPositive(input);
		//then
		assertThat(result)
			.isFalse();
	}
}
