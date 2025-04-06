package com.project.openmarket.domain.review.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.project.openmarket.domain.base.entity.BaseTime;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Consumer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document(collection = "review")
public class Review extends BaseTime {
	@Id
	private String id;

	private int rating;

	private String content;

	@DocumentReference
	private Consumer consumer;

	@DocumentReference
	private Product product;

	@Builder
	public Review(int rating, String content, Consumer consumer, Product product) {
		this.rating = rating;
		this.content = content;
		this.consumer = consumer;
		this.product = product;
	}

}
