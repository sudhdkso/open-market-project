package com.project.openmarket.domain.order.entity;

import lombok.Getter;

@Getter
public class Amount {
	private Long cache;
	private Long point;

	public Amount(Long cache, Long point){
		this.cache = cache;
		this.point = point;
	}
}
