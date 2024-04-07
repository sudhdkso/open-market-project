package com.project.openmarket.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.entity.Consumer;

class ConsumerTests {

	@Test
	@DisplayName("고객은 메서드를 통해서 캐시를 증가시킬 수 있다.")
	void successIncreaseCache(){
		//given
		var request = createConsumer("a1234@example.com","010-0123-2345");
		Consumer consumer = Consumer.of(request);
		Long expected = 1000L;
		//when
		consumer.increaseCache(1000L);
		//then
		assertThat(consumer.getCache()).isEqualTo(expected);
	}

	@Test
	@DisplayName("고객은 메서드를 통해서 캐시를 감소시킬 수 있다.")
	void successDecreaseCache(){
		//given
		var request = createConsumer("a1234@example.com","010-0123-2345");
		Consumer consumer = Consumer.of(request);

		Long expected = 500L;
		//when
		consumer.increaseCache(1000L);
		consumer.decreaseCache(500L);
		//then
		assertThat(consumer.getCache()).isEqualTo(expected);
	}

	@Test
	@DisplayName("고객은 Amount클래스를 통해서 캐시와 포인트를 증가시킬 수 있다.")
	void successIncreaseCacheAndPoint(){
		//given
		var request = createConsumer("a1234@example.com","010-0123-2345");
		Consumer consumer = Consumer.of(request);

		Amount amount = new Amount(1000L, 500L);

		Long cacheExpected = 1000L;
		Long pointExpected = 500L;
		//when
		consumer.increaseAmount(amount);
		//then
		assertThat(consumer.getCache()).isEqualTo(cacheExpected);
		assertThat(consumer.getPoint()).isEqualTo(pointExpected);
	}

	@Test
	@DisplayName("고객은 Amount클래스를 통해서 캐시와 포인트를 감소시킬 수 있다.")
	void successDecreaseCacheAndPoint(){
		//given
		var request = createConsumer("a1234@example.com","010-0123-2345");
		Consumer consumer = Consumer.of(request);

		Amount iamount = new Amount(1000L, 500L);
		Amount damount = new Amount(100L, 400L);

		Long cacheExpected = iamount.getCache()-damount.getCache();
		Long pointExpected = iamount.getPoint()-damount.getPoint();
		//when
		consumer.increaseAmount(iamount);
		consumer.decreaseAmount(damount);
		//then
		assertThat(consumer.getCache()).isEqualTo(cacheExpected);
		assertThat(consumer.getPoint()).isEqualTo(pointExpected);
	}

	ConsumerCreateReqestDto createConsumer(String email, String phoneNumber){
		String name = "고객";
		String password = "1234";
		String address = "어디지";
		return new ConsumerCreateReqestDto(email, name, phoneNumber, password, address);
	}
}
