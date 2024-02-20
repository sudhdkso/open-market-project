package com.project.openmarket.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.user.dto.reposne.UserResponseDto;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;
import com.project.openmarket.domain.user.service.SellerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SellerController {
	private final SellerService sellerService;

	@PostMapping("/seller")
	public ResponseEntity<UserResponseDto> saveSeller(@RequestBody SellerCreateRequestDto requestDto){
		UserResponseDto reponseDto = sellerService.save(requestDto);
		return ResponseEntity.ok().body(reponseDto);
	}

}
