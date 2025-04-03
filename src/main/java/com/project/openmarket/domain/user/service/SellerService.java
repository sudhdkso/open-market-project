package com.project.openmarket.domain.user.service;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		duplicatedEmail(request.email());
		Seller seller = request.toEntity();
		return UserResponseDto.of(sellerRepository.save(seller));
	}

	public Seller findById(ObjectId id){
		return sellerRepository.getById((id));
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
		Seller seller = sellerRepository.getByEmail(email);

		if(!seller.isSamePassword(password)){
			throw new CustomException(NOT_MATCH_PASSWORD);
		}

		return seller;
	}


	public void processPayment(Long amount, Seller seller){
		seller.increaseCash(amount);
		sellerRepository.save(seller);
	}
}
