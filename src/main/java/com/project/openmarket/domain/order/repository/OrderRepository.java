package com.project.openmarket.domain.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.user.entity.Consumer;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByConsumer(Consumer consumer);
}
