package com.project.openmarket.domain.user.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.exception.enums.ExceptionConstants;

public interface SellerRepository extends MongoRepository<Seller, ObjectId> {

	@Query("{ 'email' : ?0 }")
	Optional<Seller> findByEmail(String email);

	boolean existsByEmail(String email);

	default Seller getById(ObjectId id){
		return findById(id)
			.orElseThrow(() -> new CustomException(ExceptionConstants.NOT_FOUND_USER));
	}

	default Seller getByEmail(String email) {
		return findByEmail(email)
			.orElseThrow(() -> new CustomException(ExceptionConstants.NOT_FOUND_USER));
	}
}
