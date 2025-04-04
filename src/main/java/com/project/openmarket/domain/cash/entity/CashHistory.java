package com.project.openmarket.domain.cash.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import com.project.openmarket.domain.base.entity.BaseTime;
import com.project.openmarket.domain.cash.entity.enums.CashStatus;
import com.project.openmarket.domain.cash.entity.enums.PayType;
import com.project.openmarket.domain.user.entity.Consumer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document(collection = "cache_history")
public class CashHistory extends BaseTime {
	@Id
	private ObjectId id;

	@DocumentReference
	private Consumer consumer;

	private Long amount;

	@Field("status")
	private CashStatus status;

	@Field("pay_type")
	private PayType payType;

	private String orderId;

	private String orderName;

	private String paymentKey;

	@Builder
	public CashHistory(Consumer consumer, Long amount, CashStatus status, PayType payType, String orderId,
		String orderName,
		String paymentKey) {
		this.consumer = consumer;
		this.amount = amount;
		this.status = status;
		this.payType = payType;
		this.orderId = orderId;
		this.orderName = orderName;
		this.paymentKey = paymentKey;
	}

	public CashHistory(Consumer consumer, Long amount, String orderId, String paymentKey, CashStatus status) {
		this(consumer, amount, status, null, orderId, null, paymentKey);
	}

	public void updateCashStatus(CashStatus status) {
		this.status = status;
	}
}
