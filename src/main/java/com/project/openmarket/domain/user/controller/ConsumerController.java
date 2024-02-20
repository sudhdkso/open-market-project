package com.project.openmarket.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.user.dto.reposne.ConsumerResponseDto;
import com.project.openmarket.domain.user.dto.reposne.UserResponseDto;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.dto.request.ConsumerLoginRequestDto;
import com.project.openmarket.domain.user.service.ConsumerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ConsumerController {
	private final ConsumerService consumerService;

	@PostMapping("/consumer")
	public ResponseEntity<UserResponseDto> saveConsumer(@RequestBody ConsumerCreateReqestDto requestDto){
		UserResponseDto responseDto = consumerService.saveConumser(requestDto);
		return ResponseEntity.ok().body(responseDto);
	}

	@PostMapping("/consumer/login")
	public ResponseEntity<ConsumerResponseDto> login(@RequestBody ConsumerLoginRequestDto requestDto){
		ConsumerResponseDto consumerResponseDto = consumerService.login(requestDto);
		return ResponseEntity.ok().body(consumerResponseDto);
	}
}

