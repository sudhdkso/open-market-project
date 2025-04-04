package com.project.openmarket.domain.cash.service;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.cash.dto.request.PaymentRequestDto;
import com.project.openmarket.domain.cash.dto.response.PaymentSuccessResponseDto;
import com.project.openmarket.domain.cash.entity.CashHistory;
import com.project.openmarket.domain.cash.entity.enums.CashStatus;
import com.project.openmarket.domain.cash.repository.CashHistoryRepository;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.exception.enums.ExceptionConstants;
import com.project.openmarket.global.http.TossHttpClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TossPaymentService {
	private final TossHttpClient tossHttpClient;
	private final CashHistoryRepository cashHistoryRepository;
	private final ConsumerService consumerService;

	@Value("${secrets.api-secret-key}")
	private String apiSecretKey;

	@Transactional
	public PaymentSuccessResponseDto confirmPayment(Consumer consumer, JSONObject payment) throws IOException {
		String orderId = payment.get("orderId").toString();
		Long amount = Long.parseLong(payment.get("amount").toString());

		CashHistory cashHistory = cashHistoryRepository.getByOrderId(orderId);
		//저장된 가격과 요청 가격 검증
		validatePaymentAmount(amount, cashHistory.getAmount());

		JSONObject response = tossHttpClient.sendRequest(payment, apiSecretKey,
			"https://api.tosspayments.com/v1/payments/confirm");
		int statusCode = response.containsKey("error") ? 400 : 200;

		if (statusCode == 400) {
			throw new RuntimeException("Payment confirmation failed with error: " + response.get("error"));
		}

		updatePaymentStatusAndAddCash(cashHistory, amount, consumer);

		return PaymentSuccessResponseDto.of(cashHistory);
	}

	@Transactional
	public PaymentSuccessResponseDto savedTempCashHistory(Consumer consumer, PaymentRequestDto requestDto) {
		CashHistory cashHistory = cashHistoryRepository.save(requestDto.toEntity(consumer));
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
			throw new CustomException(ExceptionConstants.NOT_MATCH_CHARGE_AMOUNT);
		}
	}

}
