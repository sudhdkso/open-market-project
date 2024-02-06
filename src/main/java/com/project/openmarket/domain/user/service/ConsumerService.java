package com.project.openmarket.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.repository.ConsumerRepository;
import com.project.openmarket.domain.user.dto.reposne.UserCreateResponseDto;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ConsumerService {
	private final ConsumerRepository consumerRepository;

	@Transactional
	public UserCreateResponseDto saveConumser(ConsumerCreateReqestDto request){
		Consumer consumer = consumerRepository.save(request.toEntity());
		return UserCreateResponseDto.of(consumer);
	}

}
