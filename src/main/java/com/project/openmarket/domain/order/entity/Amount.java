package com.project.openmarket.domain.order.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document
public class Amount {
	private Long cache;

	private Long point;

	public Amount(Long cache, Long point){
		this.cache = cache;
		this.point = point;
	}

	public Long getTotalAmount(){
		return this.cache + this.point;
	}
}
