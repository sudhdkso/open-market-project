package com.project.openmarket.domain.order.repository;

public interface CustomOrderRepository {
	void updateIsReviewed(String orderId);
}