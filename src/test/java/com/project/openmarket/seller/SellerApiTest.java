package com.project.openmarket.seller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.project.openmarket.ApiTest;

public class SellerApiTest extends ApiTest {
	@Test
	@DisplayName("판매자등록요청_api_테스트")
	void 판매자등록(){
		final var response = SellerStep.판매자생성요청(SellerStep.판매자생성요청_생성());
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

}
