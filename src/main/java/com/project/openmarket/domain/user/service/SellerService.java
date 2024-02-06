package com.project.openmarket.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.user.dto.reposne.UserCreateResponseDto;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.repository.SellerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SellerService {
	private final SellerRepository sellerRepository;

	@Transactional
	public UserCreateResponseDto save(SellerCreateRequestDto request){
		Seller seller = sellerRepository.save(request.toEntity());
		return UserCreateResponseDto.of(seller);
	}

}