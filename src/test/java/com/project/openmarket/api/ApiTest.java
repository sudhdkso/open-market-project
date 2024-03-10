package com.project.openmarket.api;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest {

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		if(RestAssured.port == RestAssured.UNDEFINED_PORT) {
			RestAssured.port = port;
		}
	}
}