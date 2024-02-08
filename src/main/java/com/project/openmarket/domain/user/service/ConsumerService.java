package com.project.openmarket.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.user.dto.reposne.UserCreateResponseDto;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.repository.ConsumerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ConsumerService {
	private final ConsumerRepository consumerRepository;

	@Transactional
	public UserCreateResponseDto saveConumser(ConsumerCreateReqestDto request){
		Consumer consumer = request.toEntity();
		duplicatedEmail(consumer.getEmail());
		return UserCreateResponseDto.of(consumerRepository.save(consumer));
	}

	private void duplicatedEmail(final String email){
		if(consumerRepository.existsByEmail(email)){
			throw new IllegalArgumentException();
			// customException으로 변경
		}
	}
}
