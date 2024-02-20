package com.project.openmarket.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.user.dto.reposne.ConsumerResponseDto;
import com.project.openmarket.domain.user.dto.reposne.UserResponseDto;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.dto.request.ConsumerLoginRequestDto;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.repository.ConsumerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ConsumerService {
	private final ConsumerRepository consumerRepository;

	@Transactional
	public UserResponseDto saveConumser(ConsumerCreateReqestDto request){
		Consumer consumer = request.toEntity();
		duplicatedEmail(consumer.getEmail());
		return UserResponseDto.of(consumerRepository.save(consumer));
	}

	private void duplicatedEmail(final String email){
		if(consumerRepository.existsByEmail(email)){
			throw new IllegalArgumentException();
			// customException으로 변경
		}
	}

	@Transactional
	public ConsumerResponseDto login(ConsumerLoginRequestDto request){

		Consumer consumer = consumerRepository.findByEmail(request.email())
			.orElseThrow(() -> new IllegalArgumentException());

		System.out.println(request.email() + " "+request.password()+" "+consumer.toString());
		if(!request.password().equals(consumer.getPassword())){
			throw new IllegalArgumentException();
		}

		return ConsumerResponseDto.of(consumer);
	}
}
