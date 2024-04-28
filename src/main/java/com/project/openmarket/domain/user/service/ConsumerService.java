package com.project.openmarket.domain.user.service;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.user.dto.reposne.UserResponseDto;
import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.dto.request.LoginRequestDto;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.repository.ConsumerRepository;
import com.project.openmarket.global.exception.CustomException;

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
		Optional<Consumer> consumer = consumerRepository.findByEmail(email);
		
		if(!consumer.isPresent()){
			throw new CustomException(NOT_FOUND_USER);
		}

		return consumer
			.filter(m -> m.isSamePassword(password))
			.orElseThrow(() -> new CustomException(NOT_MATCH_PASSWORD));
	}

	public Consumer getConsumerById(Long id){
		return consumerRepository.findById(id)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
	}

	public void increaseAmount(Amount amount, Consumer consumer){
		consumer.increaseAmount(amount);
		consumerRepository.save(consumer);
	}

	public void decreaseAmount(Amount amount, Consumer consumer){
		consumer.decreaseAmount(amount);
		consumerRepository.save(consumer);
	}

	public void processPoints(Order order, Consumer consumer){
		consumer.increasePurchasePoints(order.totalAmount());
		consumerRepository.save(consumer);
	}
}
