package com.project.openmarket.domain.auth;

import com.project.openmarket.domain.user.entity.Consumer;

public class ConsumerThreadLocal {
	private static final ThreadLocal<Consumer> CONSUMER_THREAD_LOCAL = new ThreadLocal<>();

	private ConsumerThreadLocal(){

	}
	public static void set(Consumer consumer) {
		CONSUMER_THREAD_LOCAL.set(consumer);
	}

	public static Consumer get() {
		return CONSUMER_THREAD_LOCAL.get();
	}

	public static void remove() {
		CONSUMER_THREAD_LOCAL.remove();
	}
}
