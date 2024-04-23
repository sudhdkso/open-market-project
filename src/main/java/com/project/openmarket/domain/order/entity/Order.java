package com.project.openmarket.domain.order.entity;

import com.project.openmarket.domain.base.entity.BaseTime;
import com.project.openmarket.domain.order.dto.request.OrderRequestDto;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Consumer;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseTime {
	@Id
	@Column(name = "id", unique = true, nullable = false, updatable = false, columnDefinition = "BIGINT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "status", nullable = false)
	private OrderStatus status;

	@Column(name = "count", nullable = false)
	private int count;

	@Embedded
	private Amount amount;

	@ManyToOne
	@JoinColumn(name = "consumer_id")
	private Consumer consumer;

	private Order(Product product, OrderStatus status, Amount amount, int count, Consumer consumer){
		this.product = product;
		this.status = status;
		this.count = count;
		this.amount = amount;
		this.consumer = consumer;
	}

	public static Order of(Product product, OrderRequestDto dto, Consumer consumer){
		return new Order(product, OrderStatus.getOrderStatus(dto.status()), new Amount(dto.cache(), dto.point()), dto.count(), consumer);
	}

	public void updateOrderStatus(OrderStatus orderStatus){
		this.status = orderStatus;
	}

	public boolean isBeforeDeliveryStart(){
		return this.status.isBeforeDeliveryStart();
	}
}
