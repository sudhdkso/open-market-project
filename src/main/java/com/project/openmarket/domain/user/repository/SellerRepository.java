package com.project.openmarket.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.openmarket.domain.user.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
	Optional<Seller> findByEmail(String email);

	boolean existsByEmail(String email);
}
