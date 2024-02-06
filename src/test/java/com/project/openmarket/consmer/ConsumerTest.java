package com.project.openmarket.consmer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.repository.ConsumerRepository;

@DataJpaTest
class ConsumerTest {
	@Autowired
	private ConsumerRepository consumerRepository;

	@BeforeEach
	void setUp() {

	}

	@Test
	@DisplayName("단일 구매자 생성 확인")
	public void createBuyer (){
	    //given
	    String name = "가명";
		String email = "asdf@example.com";
		String phoneNumber = "010-0000-0000";
		String password = "1234";
		String address = "";


		ConsumerCreateReqestDto dto = new ConsumerCreateReqestDto(email, name, phoneNumber, address, password);
		Consumer consumer = Consumer.of(dto);

		//when

		Consumer newConsumer = consumerRepository.save(consumer);

	    //then
		assertThat(consumer).isEqualTo(newConsumer);
	}

	@Test
	@DisplayName("다중 구매자 생성 확인")
	public void createBuyers (){
		//given
		String name = "가명";
		String email = "asdf@example.com";
		String phoneNumber = "010-0000-0000";
		String address = "";
		String password = "1234";

		List<Consumer> consumers = new ArrayList<>();
		for(int i=0;i<10;i++){
			ConsumerCreateReqestDto dto = new ConsumerCreateReqestDto(i+email, name+i, phoneNumber, address, password);
			consumers.add(Consumer.of(dto));
		}

		//when

		consumerRepository.saveAll(consumers);
		int expected = 10;
		List<Consumer> savedConsumer = consumerRepository.findAll();

		//then
		assertThat(savedConsumer.size()).isEqualTo(expected);
	}
}