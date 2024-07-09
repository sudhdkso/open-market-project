package com.project.openmarket.domain.order.dto.request;

import static com.project.openmarket.global.exception.enums.ExceptionConstants.*;

import com.project.openmarket.domain.order.entity.Amount;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.entity.eums.OrderStatus;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.global.exception.CustomException;
import com.project.openmarket.global.validator.NumberValidator;

public record OrderRequestDto(Long productId, int orderedPrice, Long cache, Long point, int count) {
	public OrderRequestDto{
		if(!NumberValidator.isPositive(count)){
			throw new CustomException(NOT_POSITIVE_NUMBER);
		}
	}


	public Order toEntity(Product product, Consumer consumer) {
		return Order.builder()
			.product(product)
			.consumer(consumer)
			.status(OrderStatus.ORDER_COMPLETED)
			.amount(new Amount(cache(), point()))
			.count(count())
			.build();
	}
}
