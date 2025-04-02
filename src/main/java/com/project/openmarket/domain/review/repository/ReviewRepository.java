package com.project.openmarket.domain.review.repository;

import java.util.List;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.review.entity.Review;
import com.project.openmarket.domain.user.entity.Consumer;

public interface ReviewRepository extends MongoRepository<Review, String> {

	@Query(" 'product._id' : ?#{#product.id} ")
	List<Review> findByProduct(Product product);

	@Query(" 'consumer._id' : ?#{#consumer.id} ")
	List<Review> findByConsumer(Consumer consumer);

	// @Query(value = "select avg(r.score) from review r where r.product = :product group by r.product", nativeQuery = true)
	// double getAvgScoreByProduct(@Param("product") Product product);
}
