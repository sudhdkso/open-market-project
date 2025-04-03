package com.project.openmarket.domain.user.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.project.openmarket.domain.base.entity.BaseTime;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class User extends BaseTime {

	@Id
	private ObjectId id;

	private String email;

	private String name;

	private Long cash;

	private String phoneNumber;

	private String password;

	protected User(String email, String name, String phoneNumber, String password) {
		this.email = email;
		this.name = name;
		this.cash = 0L;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}

	public boolean isSamePassword(String another){
		return password.equals(another);
	}

	public void increaseCash(Long amount){
		this.cash += amount;
	}

	public void decreaseCash(Long amount){
		this.cash -= amount;
	}
}
