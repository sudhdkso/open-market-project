package com.project.openmarket.domain.order.repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.project.openmarket.domain.order.entity.Order;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomOrderRepositoryImpl implements CustomOrderRepository {
	private final MongoTemplate mongoTemplate;

	@Override
	public void updateIsReviewed(String orderId) {
		Query query = new Query(Criteria.where("_id").is(orderId));
		Update update = new Update().set("isReviewed", true);
		mongoTemplate.updateFirst(query, update, Order.class);
	}
}
