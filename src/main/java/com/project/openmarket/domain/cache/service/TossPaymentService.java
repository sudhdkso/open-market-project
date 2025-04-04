package com.project.openmarket.domain.cache.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.cache.dto.request.PaymentRequestDto;
import com.project.openmarket.domain.cache.dto.response.PaymentSuccessResponseDto;
import com.project.openmarket.domain.cache.entity.CashHistory;
import com.project.openmarket.domain.cache.entity.enums.CashStatus;
import com.project.openmarket.domain.cache.repository.CashHistoryRepository;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.exception.enums.ExceptionConstants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TossPaymentService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${secrets.api-secret-key}")
	private String apiSecretKey;

	private CashHistoryRepository cashHistoryRepository;
	private ConsumerService consumerService;

	@Transactional
	public PaymentSuccessResponseDto confirmPayment(Consumer consumer, JSONObject payment) throws IOException {
		String orderId = payment.get("orderId").toString();
		Long amount = Long.parseLong(payment.get("amount").toString());

		CashHistory cashHistory = cashHistoryRepository.getByOrderId(orderId);
		//저장된 가격과 요청 가격 검증
		validatePaymentAmount(amount, cashHistory.getAmount());

		JSONObject response = sendRequest(payment, apiSecretKey, "https://api.tosspayments.com/v1/payments/confirm");
		int statusCode = response.containsKey("error") ? 400 : 200;

		if (statusCode == 400) {
			throw new RuntimeException("Payment confirmation failed with error: " + response.get("error"));
		}

		updatePaymentStatusAndAddCash(cashHistory, amount, consumer);

		return PaymentSuccessResponseDto.of(cashHistory);
	}

	private void updatePaymentStatusAndAddCash(CashHistory cashHistory, Long amount, Consumer consumer) {
		// 결제 상태 업데이트
		cashHistory.updateCashStatus(CashStatus.COMPLETED);
		cashHistoryRepository.save(cashHistory);

		// 소비자에게 캐시 추가
		consumerService.increaseCash(amount, consumer);
	}

	private void validatePaymentAmount(Long amount, Long savedAmount) {
		if (!amount.equals(savedAmount)) {
			throw new CustomException(ExceptionConstants.INVALID_DATA_INPUT);
		}
	}

	public PaymentSuccessResponseDto savedTempCashHistory(Consumer consumer, PaymentRequestDto requestDto) {
		CashHistory cashHistory = cashHistoryRepository.save(requestDto.toEntity(consumer));
		return PaymentSuccessResponseDto.of(cashHistory);

	}

	private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
		HttpURLConnection connection = createConnection(secretKey, urlString);
		try (OutputStream os = connection.getOutputStream()) {
			os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
		}

		try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() :
			connection.getErrorStream();
			 Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
			return (JSONObject)new JSONParser().parse(reader);
		} catch (Exception e) {
			logger.error("Error reading response", e);
			JSONObject errorResponse = new JSONObject();
			errorResponse.put("error", "Error reading response");
			return errorResponse;
		}
	}

	private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestProperty("Authorization",
			"Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(
				StandardCharsets.UTF_8)));
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		return connection;
	}
}
