package com.project.openmarket.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.repository.OrderRepository;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.repository.ProductRepository;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.repository.ConsumerRepository;
import com.project.openmarket.domain.user.repository.SellerRepository;

@ExtendWith(MockitoExtension.class)
public abstract class ServiceTestMock {
	@Mock
	protected ConsumerRepository consumerRepository;
	@Mock
	protected SellerRepository sellerRepository;
	@Mock
	protected ProductRepository productRepository;
	@Mock
	protected OrderRepository orderRepository;

	@Mock
	protected Consumer consumer;
	@Mock
	protected Seller seller;
	@Mock
	protected Product product;
	@Mock
	protected Order order;
}
