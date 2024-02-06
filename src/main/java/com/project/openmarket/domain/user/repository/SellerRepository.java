package com.project.openmarket.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.openmarket.domain.user.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
