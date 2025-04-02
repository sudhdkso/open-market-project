package com.project.openmarket.domain.user.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.exception.enums.ExceptionConstants;

public interface SellerRepository extends MongoRepository<Seller, ObjectId> {
	Optional<Seller> findByEmail(String email);

	boolean existsByEmail(String email);

	default Seller getById(ObjectId id){
		return findById(id)
			.orElseThrow(() -> new CustomException(ExceptionConstants.NOT_FOUND_USER));
	}
}
