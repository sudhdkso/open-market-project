package com.project.openmarket.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.project.openmarket.domain.cache.entity.CashHistory;
import com.project.openmarket.domain.cache.entity.enums.CashStatus;
import com.project.openmarket.domain.user.entity.Consumer;

public class CashHistoryTests {

	@Mock
	Consumer consumer;

	@Test
	@DisplayName("cashHistory의 cashStatus를 업데이트할 수 있다.")
	void updateCashStatus() {
		//given
		CashHistory cashHistory = new CashHistory(consumer, 10000L, "orderId", "paymentKey", CashStatus.PENDING);

		//when
		cashHistory.updateCashStatus(CashStatus.COMPLETED);
		//then
		assertThat(cashHistory.getStatus()).isEqualTo(CashStatus.COMPLETED);
	}
}
