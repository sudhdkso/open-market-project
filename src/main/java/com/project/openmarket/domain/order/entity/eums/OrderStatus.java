package com.project.openmarket.domain.order.entity.eums;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum OrderStatus {
	//주문
	EMPTY("없음"),
	//주문 완료
	ORDER_COMPLETED("결제 완료"),
	//주문 접수
	//출고 처리 중
	//출고 완료
	//배송 시작
	SHIPPING("배송 시작"),
	//배송 완료
	DELIVERED("배송 완료"),
	//구매 확정
	PURCHASE_CONFIRMATION("구매 확정"),
	//주문 취소
	CANCELLED("주문 취소");

	private String value;

	OrderStatus(String value) {
		this.value = value;
	}

	public static OrderStatus getOrderStatus(String status) {
		return Arrays.stream(values())
			.filter(x -> x.getValue().equals(status))
			.findAny()
			.orElse(EMPTY);
	}

	public boolean isBeforeDeliveryStart() {
		if (this.equals(EMPTY))
			return false;
		return this.compareTo(SHIPPING) < 0;
	}
}
