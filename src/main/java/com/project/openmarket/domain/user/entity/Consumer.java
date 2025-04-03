package com.project.openmarket.domain.user.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Document("consumer")
public class Consumer extends User {
	private String address;

	private Long point;

	public Consumer(String email, String name, String phoneNumber, String address, String password){
		super(email,name, phoneNumber, password);
		this.address = address;
		this.point = 0L;
	}

	public static Consumer of(ConsumerCreateReqestDto dto){
		return new Consumer(dto.email(), dto.name(), dto.phoneNumber(), dto.address(), dto.password());
	}

	public boolean canBuy(Amount amount){
		return this.getCash() >= amount.getCash() && this.point >= amount.getPoint();
	}

	public void increaseAmount(Amount amount){
		increaseCash(amount.getCash());
		increasePoint(amount.getPoint());
	}
	
	public void increasePoint(final Long point){
		this.point += point;
	}

	public void decreaseAmount(Amount amount){
		decreaseCash(amount.getCash());
		decreasePoint(amount.getPoint());
	}

	public void decreasePoint(Long point){
		this.point -= point;
	}
}
