package com.project.openmarket.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.openmarket.domain.user.dto.reposne.UserResponseDto;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.service.ConsumerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ConsumerController {
	private final ConsumerService consumerService;

	@PostMapping("/consumer")
	public ResponseEntity<UserResponseDto> signup(@RequestBody ConsumerCreateReqestDto requestDto){
		UserResponseDto responseDto = consumerService.signup(requestDto);
		return ResponseEntity.ok().body(responseDto);
	}
}

