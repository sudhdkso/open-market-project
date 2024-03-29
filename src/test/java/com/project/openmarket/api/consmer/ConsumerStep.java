package com.project.openmarket.api.consmer;

import org.springframework.http.MediaType;

import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.dto.request.LoginRequestDto;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ConsumerStep {

	public static ExtractableResponse<Response> 고객생성요청(final ConsumerCreateReqestDto request) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when()
			.post("/api/v1/signup?role=consumer")
			.then()
			.log().all().extract();
	}

	public static ConsumerCreateReqestDto 고객생성요청_생성() {
		String name = "가명";
		String email = "asdf1@example.com";
		String phoneNumber = "010-0000-0000";
		String address = "";
		String password = "1234";
		return new ConsumerCreateReqestDto(email, name, phoneNumber, password, address);
	}

	public static ExtractableResponse<Response> 고객로그인요청(final LoginRequestDto request) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when()
			.post("/api/v1/login?role=consumer")
			.then()
			.log().all().extract();
	}

	public static LoginRequestDto 고객로그인요청_생성() {
		String email = "consumer1@example.com";
		String password = "1234";
		return new LoginRequestDto(email, password);
	}
}
