package com.project.openmarket.domain.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.user.dto.reposne.UserResponseDto;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.dto.request.LoginRequestDto;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.repository.ConsumerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ConsumerService {
	private final ConsumerRepository consumerRepository;

	@Transactional
	public UserResponseDto signup(ConsumerCreateReqestDto request){
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
	public UserResponseDto login(LoginRequestDto request){
		return UserResponseDto.of(login(request.email(),request.password()));
	}

	private Consumer login(String email, String password){
		Optional<Consumer> consumer = consumerRepository.findByEmail(email);
		System.out.println(consumerRepository.findAll().size());
		consumerRepository.findAll()
			.forEach(x -> System.out.println(x.getEmail()));
		if(!consumer.isPresent()){
			throw new IllegalArgumentException();
		}

		return consumer
			.filter(m -> m.isSamePassword(password))
			.orElseThrow(() -> new IllegalArgumentException());
	}
}
