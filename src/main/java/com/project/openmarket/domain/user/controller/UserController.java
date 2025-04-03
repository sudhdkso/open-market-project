package com.project.openmarket.domain.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.openmarket.domain.auth.ConsumerThreadLocal;
import com.project.openmarket.domain.auth.SellerThreadLocal;
import com.project.openmarket.domain.user.dto.reposne.CurrentUserResponseDto;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.entity.User;
import com.project.openmarket.domain.user.service.SellerService;
import com.project.openmarket.domain.auth.enums.Role;
import com.project.openmarket.domain.auth.enums.SessionConst;
import com.project.openmarket.domain.user.dto.reposne.UserResponseDto;
import com.project.openmarket.domain.user.dto.request.LoginRequestDto;
import com.project.openmarket.domain.user.service.ConsumerService;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.exception.enums.ExceptionConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
	private final ObjectMapper objectMapper;
	private final ConsumerService consumerService;
	private final SellerService sellerService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/signup")
	public <T>ResponseEntity<UserResponseDto> signup(
		@RequestParam("role") String role,
		@RequestBody @Valid T requestDto){
		UserResponseDto responseDto = null;

		switch (Role.findRoleByKey(role)){
			case CONSUMER ->  responseDto = processConsumerCreateRequestDto(requestDto);
			case SELLER -> responseDto = convertSellerCreateRequestDto(requestDto);
			default -> throw new CustomException(ExceptionConstants.INVALID_DATA_INPUT);
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
			default -> throw new CustomException(ExceptionConstants.INVALID_DATA_INPUT);
		}

		if(responseDto != null && responseDto.email() != null){
			HttpSession session = request.getSession();
			session.setAttribute(SessionConst.SESSION_KEY, responseDto.email());
			session.setAttribute("role", role);
			session.setMaxInactiveInterval(60 * 30);
		}
		logger.info("로그인 성공!");
		return ResponseEntity.ok().body(responseDto);
	}

	@GetMapping("/user")
	public ResponseEntity<CurrentUserResponseDto> getCurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession();

		String role = (String) session.getAttribute("role"); // 세션에서 역할 가져오기
		if (role == null) {
			logger.info("세션에 역할 정보 없음!");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		if ("seller".equals(role)) {
			Seller seller = SellerThreadLocal.get();
			if (seller != null) {
				logger.info("✅ 판매자 정보 반환");
				return ResponseEntity.ok(CurrentUserResponseDto.ofSeller(seller));
			}
		} else if ("consumer".equals(role)) {
			Consumer consumer = ConsumerThreadLocal.get();
			if (consumer != null) {
				logger.info("✅ 소비자 정보 반환");
				return ResponseEntity.ok(CurrentUserResponseDto.ofConsumer(consumer));
			}
		}

		logger.info("❌ 사용자 정보 없음!");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
