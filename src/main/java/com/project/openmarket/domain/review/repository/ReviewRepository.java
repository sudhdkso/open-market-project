package com.project.openmarket.domain.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.review.entity.Review;
import com.project.openmarket.domain.user.entity.Consumer;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findByProduct(Product product);
	List<Review> findByConsumer(Consumer consumer);

	@Query(value = "select avg(r.score) from review r where r.product = :product group by r.product", nativeQuery = true)
	long getAvgScoreByProduct(@Param("product") Product product);
}
