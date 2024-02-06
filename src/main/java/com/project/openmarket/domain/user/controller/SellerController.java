package com.project.openmarket.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.user.dto.reposne.UserCreateResponseDto;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;
import com.project.openmarket.domain.user.service.SellerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SellerController {
	private final SellerService sellerService;

	@PostMapping("/seller")
	public ResponseEntity<UserCreateResponseDto> saveSeller(@RequestBody SellerCreateRequestDto requestDto){
		UserCreateResponseDto reponseDto = sellerService.save(requestDto);
		return ResponseEntity.ok().body(reponseDto);
	}

	@GetMapping("/seller/{id}")
	public ResponseEntity<?> findSellerById(@PathVariable("id") Long id){
		return ResponseEntity.ok().body(null);
	}

	@GetMapping("/seller")
	public ResponseEntity<?> findAll(){
		return ResponseEntity.ok().body(null);
	}

}
