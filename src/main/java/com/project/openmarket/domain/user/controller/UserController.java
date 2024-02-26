package com.project.openmarket.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;
import com.project.openmarket.domain.user.service.SellerService;
import com.project.openmarket.global.auth.enums.Role;
import com.project.openmarket.global.auth.enums.SessionConst;
import com.project.openmarket.domain.user.dto.reposne.UserResponseDto;
import com.project.openmarket.domain.user.dto.request.LoginRequestDto;
import com.project.openmarket.domain.user.service.ConsumerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
	private final ObjectMapper objectMapper;
	private final ConsumerService consumerService;
	private final SellerService sellerService;

	@PostMapping("/signup")
	public <T>ResponseEntity<UserResponseDto> signup(
		@RequestParam("role") String role,
		@RequestBody T requestDto){
		UserResponseDto responseDto = null;

		switch (Role.findRoleByKey(role)){
			case CONSUMER ->  responseDto = processConsumerCreateRequestDto(requestDto);
			case SELLER -> responseDto = convertSellerCreateRequestDto(requestDto);
			default -> throw new IllegalArgumentException();
		}

		return ResponseEntity.ok().body(responseDto);
	}
	@PostMapping("/login")
	public ResponseEntity<UserResponseDto> login(
		@RequestParam("role") String role,
		@RequestBody LoginRequestDto requestDto,
		HttpServletRequest request){

		UserResponseDto responseDto = null;

		switch (Role.findRoleByKey(role)){
			case CONSUMER -> responseDto = consumerService.login(requestDto);
			case SELLER -> responseDto = sellerService.login(requestDto);
			default -> throw new IllegalArgumentException();
		}

		if(responseDto != null && responseDto.email() != null){
			HttpSession session = request.getSession();
			session.setAttribute(SessionConst.SESSION_KEY, responseDto.email());
			session.setMaxInactiveInterval(60 * 30);
		}
		return ResponseEntity.ok().body(responseDto);
	}

	private <T>UserResponseDto processConsumerCreateRequestDto(T requestDto){
		ConsumerCreateReqestDto convertDto = objectMapper.convertValue(requestDto, ConsumerCreateReqestDto.class);
		return consumerService.save(convertDto);
	}

	private <T>UserResponseDto convertSellerCreateRequestDto(T requestDto){
		SellerCreateRequestDto convertDto = objectMapper.convertValue(requestDto, SellerCreateRequestDto.class);
		return sellerService.save(convertDto);
	}
}
