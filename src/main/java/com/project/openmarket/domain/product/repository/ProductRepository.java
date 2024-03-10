package com.project.openmarket.domain.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Seller;

public interface ProductRepository extends JpaRepository<Product, Long> {

	boolean existsByNameAndSeller(String name, Seller seller);

	Optional<Product> findByNameAndSeller(String name, Seller seller);

}
