package com.project.openmarket.entity;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.entity.Consumer;

class ConsumerTests {

	@Test
	@DisplayName("고객은 메서드를 통해서 캐시를 증가시킬 수 있다.")
	void whenValidInput_thenCashIsIncreased() {
		//given
		var request = createConsumer("a1234@example.com", "010-0123-2345");
		Consumer consumer = Consumer.of(request);
		Long expected = 1000L;
		//when
		consumer.increaseCash(1000L);
		//then
		assertThat(consumer.getCash()).isEqualTo(expected);
	}

	@Test
	@DisplayName("고객은 메서드를 통해서 캐시를 감소시킬 수 있다.")
	void whenValidInput_thenCashIsDecreased() {
		//given
		var request = createConsumer("a1234@example.com", "010-0123-2345");
		Consumer consumer = Consumer.of(request);

		Long expected = 500L;
		//when
		consumer.increaseCash(1000L);
		consumer.decreaseCash(500L);
		//then
		assertThat(consumer.getCash()).isEqualTo(expected);
	}

	@Test
	@DisplayName("고객은 Amount클래스를 통해서 캐시와 포인트를 증가시킬 수 있다.")
	void whenValidInput_thenCashAndPointAreIncreased() {
		//given
		var request = createConsumer("a1234@example.com", "010-0123-2345");
		Consumer consumer = Consumer.of(request);

		Amount amount = new Amount(1000L, 500L);

		Long cacheExpected = 1000L;
		Long pointExpected = 500L;
		//when
		consumer.increaseAmount(amount);
		//then
		assertThat(consumer.getCash()).isEqualTo(cacheExpected);
		assertThat(consumer.getPoint()).isEqualTo(pointExpected);
	}

	@Test
	@DisplayName("고객은 Amount클래스를 통해서 캐시와 포인트를 감소시킬 수 있다.")
	void whenValidInput_thenCashAndPointAreDecreased() {
		//given
		var request = createConsumer("a1234@example.com", "010-0123-2345");
		Consumer consumer = Consumer.of(request);

		Amount iamount = new Amount(1000L, 500L);
		Amount damount = new Amount(100L, 400L);

		Long cacheExpected = iamount.getCash() - damount.getCash();
		Long pointExpected = iamount.getPoint() - damount.getPoint();
		//when
		consumer.increaseAmount(iamount);
		consumer.decreaseAmount(damount);
		//then
		assertThat(consumer.getCash()).isEqualTo(cacheExpected);
		assertThat(consumer.getPoint()).isEqualTo(pointExpected);
	}

	@Test
	@DisplayName("캐시랑 포인트가 요구값보다 클 때 true를 return한다.")
	void whenCashAndPointAreEnough_thenReturnTrue() {
		//given
		Consumer consumer = mock(Consumer.class);
		given(consumer.getCash()).willReturn(10000L);
		given(consumer.getPoint()).willReturn(10000L);

		Amount amount = new Amount(10000L, 10000L);

		//when
		boolean result = consumer.canBuy(amount);

		//then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("캐시랑 포인트가 요구값보다 작을떄 false return한다.")
	void whenCashAndPointAreNotEnough_thenReturnFalse() {
		//given
		Consumer consumer = mock(Consumer.class);
		given(consumer.getCash()).willReturn(1000L);
		given(consumer.getPoint()).willReturn(0L);

		Amount amount = new Amount(10000L, 10000L);

		//when
		boolean result = consumer.canBuy(amount);

		//then
		assertThat(result).isFalse();
	}

	ConsumerCreateReqestDto createConsumer(String email, String phoneNumber) {
		String name = "고객";
		String password = "1234";
		String address = "어디지";
		return new ConsumerCreateReqestDto(email, name, phoneNumber, password, address);
	}
}
