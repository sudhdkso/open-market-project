package com.project.openmarket.domain.review.event;

import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class ReviewCreatedEvent extends ApplicationEvent {
	private final ObjectId productId;

	public ReviewCreatedEvent(Object source, ObjectId productId) {
		super(source);
		this.productId = productId;
	}

}
