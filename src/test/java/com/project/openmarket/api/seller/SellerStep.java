package com.project.openmarket.api.seller;

import org.springframework.http.MediaType;

import com.project.openmarket.domain.user.dto.request.LoginRequestDto;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class SellerStep {
	public static ExtractableResponse<Response> 판매자생성요청(final SellerCreateRequestDto request) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when()
			.post("/api/v1/signup?role=seller")
			.then()
			.log().all().extract();
	}

	public static SellerCreateRequestDto 판매자생성요청_생성() {
		String name = "가명";
		String email = "asdf4@example.com";
		String phoneNumber = "010-0000-0000";
		String password = "1234";
		return new SellerCreateRequestDto(email, name, phoneNumber, password);
	}

	public static ExtractableResponse<Response> 판매자로그인요청(final LoginRequestDto request) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when()
			.post("/api/v1/login?role=seller")
			.then()
			.log().all().extract();
	}


	public static LoginRequestDto 판매자로그인요청_생성() {
		String email = "seller1@example.com";
		String password = "1234";
		return new LoginRequestDto(email, password);
	}
}
