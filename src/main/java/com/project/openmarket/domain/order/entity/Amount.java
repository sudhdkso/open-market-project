package com.project.openmarket.domain.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Amount {
	@Column(name = "cache", nullable = false)
	private Long cache;
	@Column(name = "point")
	private Long point;

	public Amount(Long cache, Long point){
		this.cache = cache;
		this.point = point;
	}

	public Long getTotalAmount(){
		return this.cache + this.point;
	}
}
