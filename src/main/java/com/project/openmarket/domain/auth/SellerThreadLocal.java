package com.project.openmarket.domain.auth;


import com.project.openmarket.domain.user.entity.Seller;

public class SellerThreadLocal {
	private static final ThreadLocal<Seller> SELLER_THREAD_LOCAL = new ThreadLocal<>();

	private SellerThreadLocal(){

	}
	public static void set(Seller seller) {
		SELLER_THREAD_LOCAL.set(seller);
	}

	public static Seller get() {
		return SELLER_THREAD_LOCAL.get();
	}

	public static void remove() {
		SELLER_THREAD_LOCAL.remove();
	}
}
