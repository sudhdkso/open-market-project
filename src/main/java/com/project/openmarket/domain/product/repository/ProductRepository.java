package com.project.openmarket.domain.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Seller;

import jakarta.persistence.LockModeType;

public interface ProductRepository extends JpaRepository<Product, Long> {

	boolean existsByNameAndSeller(String name, Seller seller);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from Product p where p.id = :id")
	Optional<Product> findByIdWithLock(@Param("id")Long id);

	Optional<Product> findByNameAndSeller(String name, Seller seller);

	List<Product> findByNameContainsOrderByAvgScoreDesc(String name);
	List<Product> findByNameContains(String name,Pageable pageable);

	Page<Product> findAll(Pageable pageable);

}
