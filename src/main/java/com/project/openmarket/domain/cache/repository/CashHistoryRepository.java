package com.project.openmarket.domain.cache.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.openmarket.domain.cache.entity.CashHistory;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.exception.enums.ExceptionConstants;

public interface CashHistoryRepository extends MongoRepository<CashHistory, ObjectId> {

	Optional<CashHistory> findByOrderId(String orderId);

	default CashHistory getByOrderId(String orderId){
		return findByOrderId(orderId)
			.orElseThrow(() -> new CustomException(ExceptionConstants.NOT_FOUND_ORDER));
	}
}
