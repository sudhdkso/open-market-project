package com.project.openmarket.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.openmarket.domain.user.entity.Consumer;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
	public boolean existsByEmail(String email);

}
