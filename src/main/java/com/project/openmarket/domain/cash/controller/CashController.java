package com.project.openmarket.domain.cash.controller;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.cash.dto.request.PaymentRequestDto;
import com.project.openmarket.domain.cash.dto.response.PaymentFailResponseDto;
import com.project.openmarket.domain.cash.dto.response.PaymentSuccessResponseDto;
import com.project.openmarket.domain.cash.service.TossPaymentService;
import com.project.openmarket.global.context.ConsumerThreadLocal;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/consumer/payment")
public class CashController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final TossPaymentService tossPaymentService;

	@PostMapping("/success")
	public ResponseEntity<PaymentSuccessResponseDto> tossConfirmPayment(HttpServletRequest request,
		@RequestBody String jsonBody) throws
		IOException {
		PaymentSuccessResponseDto response = tossPaymentService.confirmPayment(ConsumerThreadLocal.get(),
			parseRequestData(jsonBody));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/fail")
	public ResponseEntity<PaymentFailResponseDto> tossPaymentFail(@RequestParam("message") String message,
		@RequestParam("code") String code, @RequestParam("orderId") String orderId) {
		PaymentFailResponseDto response = new PaymentFailResponseDto(code, message, orderId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/verify")
	public ResponseEntity<PaymentSuccessResponseDto> tossPaymentVerification(
		@RequestBody PaymentRequestDto requestDto) {
		PaymentSuccessResponseDto response = tossPaymentService.savedTempCashHistory(ConsumerThreadLocal.get(),
			requestDto);
		return ResponseEntity.ok(response);
	}

	private JSONObject parseRequestData(String jsonBody) {
		try {
			return (JSONObject)new JSONParser().parse(jsonBody);
		} catch (ParseException e) {
			logger.error("JSON Parsing Error", e);
			return new JSONObject();
		}
	}

}
