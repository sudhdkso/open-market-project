package com.project.openmarket.service.order;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.project.openmarket.domain.order.dto.request.OrderRequestDto;
import com.project.openmarket.domain.order.entity.Order;
import com.project.openmarket.domain.order.repository.OrderRepository;
import com.project.openmarket.domain.order.service.ConsumerOrderService;
import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.dto.request.ProductUpdateReqeustDto;
import com.project.openmarket.domain.product.entity.Product;
import com.project.openmarket.domain.product.repository.ProductRepository;
import com.project.openmarket.domain.product.service.ProductService;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.repository.ConsumerRepository;
import com.project.openmarket.domain.user.repository.SellerRepository;
import com.project.openmarket.global.exception.enums.ExceptionConstants;

@SpringBootTest
class OrderServiceLockTest {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ConsumerOrderService orderService;

	@Autowired
	private ConsumerRepository consumerRepository;

	@Autowired
	private ProductService productService;

	private Consumer consumer;

	private Seller seller;

	@BeforeEach
	void setUp() {
		// 초기화 전에 기존 데이터를 삭제
		productRepository.deleteAll();
		orderRepository.deleteAll();

		seller = sellerRepository.findAll().get(0);
		// 테스트용 제품 생성
		Product product = Product.of(createRequest(1000), seller);
		productRepository.save(product);

		consumer = consumerRepository.findAll().get(0);
		consumer.increaseCache(1000L);
		consumer = consumerRepository.save(consumer);
	}

	@Test
	@DisplayName("고객이 주문 생성시 가격과 물건 가격이 같으면 주문을 생성할 수 있다.")
	@Transactional
	void testCreateOrderWithCorrectPrice() {
		Product product = productRepository.findAll().get(0);
		Long orderedPrice = 1000L;
		var request = createOrder(product.getId());

		// 정상적인 주문 생성
		orderService.create(request, consumer);

		Optional<Order> order = orderRepository.findAll().stream().findFirst();
		assertThat(order.isPresent());
		assertThat(orderedPrice).isEqualTo(order.get().getProduct().getPrice());
		assertThat(product.getId()).isEqualTo(order.get().getProduct().getId());
	}

	@Test
	@Transactional
	@DisplayName("고객의 주문 생성시 주문한 가격과 다르면 오류가 발생한다.")
	void testCreateOrderWithIncorrectPrice() {
		Product product = productRepository.findAll().get(0);
		int incorrectPrice = 10000;

		product.update(new ProductUpdateReqeustDto(0L,null, incorrectPrice, 1));
		productRepository.save(product);

		var request = createOrder(product.getId());

		assertThatThrownBy(() -> orderService.create(request,consumer))
			.hasMessage(ExceptionConstants.NOT_MATCH_PRICE.getMessage());
	}


	@Test
	@DisplayName("고객이 주문을 생성하는 과정에서 물건의 가격이 변동되지 않는다.")
	@Transactional
	void testCreateOrderAfterProductUpdateWithLock() throws InterruptedException {
		Product product = productRepository.findAll().get(0);
		Long productId = product.getId();

		var request = createOrder(productId);

		Thread thread1 = new Thread(() -> orderService.create(request, consumer));
		thread1.start();

		Thread.sleep(1000);
		int newPrice = 10000;
		var requestProductUpdate = new ProductUpdateReqeustDto(productId, null, newPrice, 1);
		productService.update(requestProductUpdate, seller);

		thread1.join();

		Product finalProduct = productRepository.findById(productId).orElseThrow();
		assertThat(product.getPrice()).isEqualTo(finalProduct.getPrice());
	}

	ProductRequestDto createRequest(int price){
		return new ProductRequestDto("Test Product", price, 1);
	}

	OrderRequestDto createOrder(Long productId){
		return new OrderRequestDto(productId, 1000, 1000L, 0L, 1);
	}
}
