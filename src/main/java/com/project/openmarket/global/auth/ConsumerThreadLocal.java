package com.project.openmarket.global.auth;

import com.project.openmarket.domain.user.entity.Consumer;

public class ConsumerThreadLocal {
	private static final ThreadLocal<Consumer> consumerThreadLocal = new ThreadLocal<>();

	public static void set(Consumer consumer) {
		consumerThreadLocal.set(consumer);
	}

	public static Consumer get() {
		return consumerThreadLocal.get();
	}

	public static void remove() {
		consumerThreadLocal.remove();
	}
}
