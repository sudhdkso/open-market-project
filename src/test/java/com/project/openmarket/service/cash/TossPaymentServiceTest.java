package com.project.openmarket.service.cash;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.openmarket.domain.cash.dto.request.PaymentRequestDto;
import com.project.openmarket.domain.cash.dto.response.PaymentSuccessResponseDto;
import com.project.openmarket.domain.cash.entity.CashHistory;
import com.project.openmarket.domain.cash.entity.enums.CashStatus;
import com.project.openmarket.domain.cash.repository.CashHistoryRepository;
import com.project.openmarket.domain.cash.service.TossPaymentService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.exception.enums.ExceptionConstants;
import com.project.openmarket.global.http.TossHttpClient;
import com.project.openmarket.service.ServiceTestMock;

@ExtendWith(MockitoExtension.class)
class TossPaymentServiceTest extends ServiceTestMock {
	@InjectMocks
	TossPaymentService tossPaymentService;
	@Mock
	CashHistoryRepository cashHistoryRepository;
	@Mock
	ConsumerService consumerService;
	@Mock
	TossHttpClient tossHttpClient;

	@DisplayName("cashHistory의 amount와 구매 확정 요청에 amount가 같으면 구매 확정 요청에 성공한다.")
	@Test
	void whenPaymentAmountMatches_thenConfirmPayment() throws IOException {
		//given
		JSONObject payment = new JSONObject();
		payment.put("orderId", "test-order-id");
		payment.put("paymentKey", "test-payment-key");
		payment.put("amount", "10000");

		CashHistory cashHistory = new CashHistory(consumer, 10000L, "test-order-id", "test-payment-key",
			CashStatus.PENDING);

		given(cashHistoryRepository.getByOrderId(anyString())).willReturn(cashHistory);

		given(tossHttpClient.sendRequest(any(), any(), any())).willReturn(new JSONObject());

		//when
		PaymentSuccessResponseDto result = tossPaymentService.confirmPayment(consumer, payment);

		// then
		assertThat(cashHistory.getStatus()).isEqualTo(CashStatus.COMPLETED);
		assertThat(result).isEqualTo(PaymentSuccessResponseDto.of(cashHistory));

		then(cashHistoryRepository)
			.should(times(1))
			.save(any());

		then(consumerService)
			.should(times(1))
			.increaseCash(any(), any(Consumer.class));
	}

	@DisplayName("cashHistory의 amount와 구매 확정 요청에 amount가 다르면 구매 확정 요청에 실패한다.")
	@Test
	void whenPaymentAmountDoesNotMatch_thenThrowException() {
		//given
		JSONObject payment = new JSONObject();
		payment.put("orderId", "test-order-id");
		payment.put("paymentKey", "test-payment-key");
		payment.put("amount", "10000");

		CashHistory cashHistory = new CashHistory(consumer, 1000L, "test-order-id", "test-payment-key",
			CashStatus.PENDING);

		given(cashHistoryRepository.getByOrderId(anyString())).willReturn(cashHistory);

		//when & then
		assertThatThrownBy(() -> tossPaymentService.confirmPayment(consumer, payment))
			.isInstanceOf(CustomException.class)
			.hasMessage(ExceptionConstants.NOT_MATCH_CHARGE_AMOUNT.getMessage());
	}

	@Test
	@DisplayName("결제 요청 응답에 에러가 발생하면 예외를 발생시킨다.")
	void whenTossApiReturnsError_thenThrowException() throws IOException {
		//given
		JSONObject payment = new JSONObject();
		payment.put("orderId", "test-order-id");
		payment.put("paymentKey", "test-payment-key");
		payment.put("amount", "10000");

		CashHistory cashHistory = new CashHistory(consumer, 10000L, "test-order-id", "test-payment-key",
			CashStatus.PENDING);

		given(cashHistoryRepository.getByOrderId(anyString())).willReturn(cashHistory);

		JSONObject response = new JSONObject();
		response.put("error", "Invalid payment");
		given(tossHttpClient.sendRequest(any(), any(), any())).willReturn(response);

		//when & then
		assertThatThrownBy(() -> tossPaymentService.confirmPayment(consumer, payment))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("error");
	}

	@Test
	@DisplayName("amount의 유효성 확인을 위해 cashHistory를 임시로 저장한다.")
	void whenPaymentDataIsValid_thenTempCashHistoryIsSaved() {
		//given
		PaymentRequestDto request = new PaymentRequestDto("test-order-id", "10000");
		given(cashHistoryRepository.save(any())).willReturn(request.toEntity(consumer));

		//when
		PaymentSuccessResponseDto response = tossPaymentService.savedTempCashHistory(consumer, request);

		//then
		assertThat(response.orderId()).isEqualTo("test-order-id");
		assertThat(response.amaount()).isEqualTo(10000L);

		then(cashHistoryRepository)
			.should(times(1))
			.save(any());
	}
}