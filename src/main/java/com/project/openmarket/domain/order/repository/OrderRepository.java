package com.project.openmarket.domain.order.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.user.entity.Consumer;

public interface OrderRepository extends MongoRepository<Order, String> {

	List<Order> findByConsumer(Consumer consumer);

	@Query("{'status': ?0, 'deliveryCompleteTime': { $lte: ?1 }}")
	List<Order> findByStatusAndDeliveryCompleteTimeBefore(@Param("threshold") LocalDateTime threshold);

	@Query("{ 'product.seller': ?0 }")
	List<Order> findBySeller(ObjectId sellerId);

	@Query("{ 'product' : { $in: ?0 } }")
	List<Order> findBySellerProducts(List<ObjectId> productIds);

}
