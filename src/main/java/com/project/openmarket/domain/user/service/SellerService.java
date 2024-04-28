package com.project.openmarket.domain.user.service;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.user.dto.reposne.UserResponseDto;
import com.project.openmarket.domain.user.dto.request.LoginRequestDto;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.repository.SellerRepository;
import com.project.openmarket.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SellerService {
	private final SellerRepository sellerRepository;

	@Transactional
	public UserResponseDto save(SellerCreateRequestDto request){
		Seller seller = request.toEntity();
		duplicatedEmail(seller.getEmail());
		return UserResponseDto.of(sellerRepository.save(seller));
	}

	public Seller findById(Long sellerId){
		return sellerRepository.findById(sellerId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
	}

	private void duplicatedEmail(final String email){
		if(sellerRepository.existsByEmail(email)){
			throw new CustomException(ALREADY_EXISTS_EMAIL);
		}
	}

	@Transactional
	public UserResponseDto login(LoginRequestDto request){
		return UserResponseDto.of(login(request.email(), request.password()));
	}

	public Seller login(String email, String password){
		Optional<Seller> seller = sellerRepository.findByEmail(email);
		if(!seller.isPresent()){
			throw new CustomException(NOT_FOUND_USER);
		}

		return seller
			.filter(m -> m.isSamePassword(password))
			.orElseThrow(() -> new CustomException(NOT_MATCH_PASSWORD));
	}


	public void processPayment(Order order, Seller seller){
		seller.increaseCacheBalance(order.totalAmount());
		sellerRepository.save(seller);
	}
}
