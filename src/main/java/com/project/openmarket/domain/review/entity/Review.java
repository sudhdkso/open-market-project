package com.project.openmarket.domain.review.entity;

import org.checkerframework.checker.units.qual.C;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import com.project.openmarket.domain.base.entity.BaseTime;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.review.dto.request.ReviewCreateResponseDto;
import com.project.openmarket.domain.review.entity.enums.Score;
import com.project.openmarket.domain.user.entity.Consumer;

import lombok.Builder;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Document(collection = "review")
public class Review extends BaseTime {
	@Id
	private String id;

	@Field("score")
	private Score score;

	@DocumentReference
	private Consumer consumer;

	@DocumentReference
	private Product product;

	@DocumentReference
	private Order order;

	@Builder
	private Review(Score score, Consumer consumer, Product product, Order order){
		this.score = score;
		this.consumer = consumer;
		this.product = product;
		this.order = order;
	}

	public static Review of(ReviewCreateResponseDto dto, Consumer consumer) {
		return new Review(Score.getScoreByValue(dto.score()), consumer, dto.order().getProduct(), dto.order());
	}


}
