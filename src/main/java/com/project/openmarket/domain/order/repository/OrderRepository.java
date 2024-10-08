package com.project.openmarket.domain.order.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.user.entity.Consumer;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByConsumer(Consumer consumer);
	@Query("select o from Order o where o.status = 'DELIVERY_COMPLETED' "
		+ "AND o.deliveryCompleteTime <= :threshold")
	List<Order> findOrdersWithDeliveryCompleteTimeExceedingThreshold(@Param("threshold")LocalDateTime threshold);

	@Query("SELECT o FROM Order o WHERE o.product.seller.id = :sellerId")
	List<Order> findOrdersBySellerId(@Param("sellerId") Long sellerId);

}
