package com.project.openmarket.domain.user.dto.request;

import com.project.openmarket.domain.user.entity.Consumer;

public record ConsumerCreateReqestDto(String email, String name,  String phoneNumber, String password, String address){
	public Consumer toEntity(){
		return Consumer.of(this);
	}
}
