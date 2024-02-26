package com.project.openmarket.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	private final ConsumerService consumerService;

	@PostMapping("/login")
	public ResponseEntity<UserResponseDto> login(
		@RequestParam("role") String role,
		@RequestBody LoginRequestDto requestDto,
		HttpServletRequest request){
		Role rl =  Role.findRoleByKey(role);

		UserResponseDto userResponseDto = null;

		if(rl.equals(Role.CONSUMER)){
			userResponseDto = consumerService.login(requestDto);
		}

		if(userResponseDto != null && userResponseDto.email() != null){
			HttpSession session = request.getSession();
			session.setAttribute(SessionConst.SESSION_KEY, userResponseDto.email());
			session.setMaxInactiveInterval(60 * 30);
		}
		return ResponseEntity.ok().body(userResponseDto);
	}
}
