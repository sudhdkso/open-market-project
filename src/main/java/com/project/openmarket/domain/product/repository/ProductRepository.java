package com.project.openmarket.domain.product.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.exception.enums.ExceptionConstants;

public interface ProductRepository extends MongoRepository<Product, ObjectId> {

	@Query(value = "{ 'name': ?0, 'seller._id': ?#{#seller.id} }", count = true)
	Long countByNameAndSeller(String name, @Param("seller") Seller seller);

	//	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("{ 'id' : ?0 }")
	Optional<Product> findByIdWithLock(@Param("id") ObjectId id);

	List<Product> findBySeller(Seller seller);

	List<Product> findByNameContainsOrderByAvgScoreDesc(String name);

	Page<Product> findByNameContaining(String name, Pageable pageable);

	@NotNull Page<Product> findAll(@NotNull Pageable pageable);

	default Product getById(ObjectId id) {
		return findById(id)
			.orElseThrow(() -> new CustomException(ExceptionConstants.NOT_FOUND_PRODUCT));

	}

	default boolean existsByNameAndSeller(String name, Seller seller) {
		Long count = countByNameAndSeller(name, seller);
		return count != null && count > 0;
	}
}
