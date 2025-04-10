package com.project.openmarket.domain.review.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.review.entity.Review;
import com.project.openmarket.domain.user.entity.Consumer;

public interface ReviewRepository extends MongoRepository<Review, String>, CustomReviewRepository {

	@Query(" 'product' : ?0 ")
	List<Review> findByProduct(Product product);

	@Query(" 'consumer' : ?0 ")
	List<Review> findByConsumer(Consumer consumer);
}
