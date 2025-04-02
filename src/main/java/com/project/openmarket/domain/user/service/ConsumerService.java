package com.project.openmarket.domain.user.service;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.user.dto.reposne.UserResponseDto;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.dto.request.LoginRequestDto;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.repository.ConsumerRepository;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.exception.enums.ExceptionConstants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ConsumerService {
	private final ConsumerRepository consumerRepository;

	@Transactional
	public UserResponseDto save(ConsumerCreateReqestDto request){
		Consumer consumer = request.toEntity();
		duplicatedEmail(consumer.getEmail());
		return UserResponseDto.of(consumerRepository.save(consumer));
	}

	private void duplicatedEmail(final String email){
		if(consumerRepository.existsByEmail(email)){
			throw new CustomException(ALREADY_EXISTS_EMAIL);
		}
	}

	@Transactional
	public UserResponseDto login(LoginRequestDto request){
		return UserResponseDto.of(login(request.email(),request.password()));
	}

	private Consumer login(String email, String password){
		Consumer consumer = consumerRepository.getByEmail(email);

		if(!consumer.isSamePassword(password)){
			throw new CustomException(NOT_MATCH_PASSWORD);
		}

		return consumer;
	}

	public Consumer getConsumerById(ObjectId id){
		return consumerRepository.getById(id);
	}

	public void increaseAmount(Amount amount, Consumer consumer){
		consumer.increaseAmount(amount);
		consumerRepository.save(consumer);
	}

	public void decreaseAmount(Amount amount, Consumer consumer){
		consumer.decreaseAmount(amount);
		consumerRepository.save(consumer);
	}

	public void processPoints(Long amount, Consumer consumer){
		consumer.increasePoint(amount);
		consumerRepository.save(consumer);
	}
}
