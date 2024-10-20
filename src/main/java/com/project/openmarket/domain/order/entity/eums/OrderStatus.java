package com.project.openmarket.domain.order.entity.eums;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum OrderStatus {
	//주문
	EMPTY("없음"),
	//주문 완료
	ORDER_COMPLETED("주문 완료"),
	//주문 접수
	//출고 처리 중
	//출고 완료
	//배송 시작
	DELIVERY_START("배송 시작"),
	//배송 완료
	DELIVERT_COMPLETED("배송 완료"),
	//구매 확정
	PURCHASE_CONFIRMATION("구매 확정"),
	//주문 취소
	CANCEL("주문 취소");

	private String status;

	OrderStatus(String status){
		this.status = status;
	}

	public static OrderStatus getOrderStatus(String status){
		return Arrays.stream(values())
			.filter(x -> x.getStatus().equals(status))
			.findAny()
			.orElse(EMPTY);
	}

	public boolean isBeforeDeliveryStart(){
		if(this.equals(EMPTY)) return false;
		return this.compareTo(DELIVERY_START) < 0;
	}
}
