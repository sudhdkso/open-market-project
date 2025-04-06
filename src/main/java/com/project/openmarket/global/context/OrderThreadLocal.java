package com.project.openmarket.global.context;

import com.project.openmarket.domain.order.entity.Order;

public class OrderThreadLocal {
	private static final ThreadLocal<Order> orderThreadLocal = new ThreadLocal<>();

	public static void set(Order order) {
		orderThreadLocal.set(order);
	}

	public static Order get() {
		return orderThreadLocal.get();
	}

	public static void clear() {
		orderThreadLocal.remove();
	}
}
