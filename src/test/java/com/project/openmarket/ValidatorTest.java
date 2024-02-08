package com.project.openmarket;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.project.openmarket.validator.Validator;

public class ValidatorTest {
	@Test
	@DisplayName("이메일_형식_테스트")
	void wrongEmailPattern(){
		//given
		String email = "asdf3@example";

		//when
		Throwable thrown = catchThrowable(() -> Validator.validateEmail(email));

		//then
		assertThat(thrown)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("틀린_휴대전화_형식_테스트")
	void wrongPhoneNumberPattern(){
		//given
		String phoneNumber = "010-1234-56";
		// when
		Throwable thrown = catchThrowable(() -> Validator.validatePhoneNumber(phoneNumber));

		//then
		assertThat(thrown)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("휴대전화_형식_문자포함")
	void phoneNumberIncludeChar(){
		//given
		String phoneNumber = "010-0000-xxxx";
		// when
		Throwable thrown = catchThrowable(() -> Validator.validatePhoneNumber(phoneNumber));

		//then
		assertThat(thrown)
			.isInstanceOf(IllegalArgumentException.class);

	}
}
