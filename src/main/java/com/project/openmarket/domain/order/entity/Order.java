package com.project.openmarket.domain.order.entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import com.project.openmarket.domain.base.entity.BaseTime;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Consumer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document(collection = "order")
public class Order extends BaseTime {
	@Id
	private String id;

	@DocumentReference
	private Product product;

	@Field("status")
	private OrderStatus status;

	private int count;

	private int orderedPrice;

	private Amount amount;

	@DocumentReference
	private Consumer consumer;

	private LocalDateTime deliveryCompletedAt;

	private String deliveryAddress;

	private boolean isReviewed;

	@Builder
	public Order(Product product, OrderStatus status, Amount amount, int count, Consumer consumer) {
		this.product = product;
		this.status = status;
		this.count = count;
		this.orderedPrice = amount.getTotalAmount().intValue();
		this.amount = amount;
		this.deliveryAddress = consumer.getAddress();
		this.consumer = consumer;
		this.isReviewed = true;
	}

	public void confirmPurchase() {
		updateOrderStatus(OrderStatus.PURCHASE_CONFIRMATION);
	}

	public void updateOrderStatus(OrderStatus orderStatus) {
		this.status = orderStatus;
	}

	public Long getTotalAmount() {
		return amount.getTotalAmount();
	}

	public boolean isBeforeDeliveryStart() {
		return this.status.isBeforeDeliveryStart();
	}

	public boolean isDeliveryCompleted() {
		return this.status.equals(OrderStatus.DELIVERED);
	}

	public boolean isPurchaseConfirmed() {
		return this.status.equals(OrderStatus.PURCHASE_CONFIRMATION);
	}

	public ObjectId getSellerId() {
		return this.product.getSellerId();
	}

	public ObjectId getConsumerId() {
		return this.consumer.getId();
	}

	public void completeOrderDelivery() {
		completeOrderDelivery(LocalDateTime.now());
	}

	public boolean isEqualPriceTo(int price) {
		return this.orderedPrice == price;
	}

	public void completeOrderDelivery(LocalDateTime dateTime) {
		this.deliveryCompletedAt = dateTime;
		updateOrderStatus(OrderStatus.DELIVERED);
	}

	public void updateReviewed(boolean isReviewed) {
		this.isReviewed = isReviewed;
	}
}
