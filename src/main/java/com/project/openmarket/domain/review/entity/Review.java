package com.project.openmarket.domain.review.entity;

import com.project.openmarket.domain.base.entity.BaseTime;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.review.dto.request.ReviewCreateResponseDto;
import com.project.openmarket.domain.review.entity.enums.Score;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.global.util.Converter.ScoreConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "review")
public class Review extends BaseTime {
	@Id
	@Column(name = "id", unique = true, nullable = false, updatable = false, columnDefinition = "BIGINT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Convert(converter = ScoreConverter.class)
	@Column(name = "score")
	private Score score;

	@ManyToOne
	@JoinColumn(name = "consumer_id")
	private Consumer consumer;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

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
