package com.project.openmarket.domain.order.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document
public class Amount {
	private Long cash;

	private Long point;

	public Amount(Long cash, Long point) {
		this.cash = cash;
		this.point = point;
	}

	public Long getTotalAmount() {
		return this.cash + this.point;
	}
}
