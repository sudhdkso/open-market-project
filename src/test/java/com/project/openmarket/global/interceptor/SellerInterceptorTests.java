package com.project.openmarket.global.interceptor;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpSession;

import com.project.openmarket.domain.user.dto.request.SellerCreateRequestDto;
import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.repository.SellerRepository;
import com.project.openmarket.domain.auth.ConsumerThreadLocal;
import com.project.openmarket.domain.auth.SellerThreadLocal;
import com.project.openmarket.domain.auth.enums.SessionConst;

class SellerInterceptorTests extends InterceptorTestMock {
	@InjectMocks
	private SellerInterceptor sellerInterceptor;
	@Mock
	private SellerRepository sellerRepository;
	private static MockHttpSession session;

	@BeforeEach
	void setUp() {
		//판매자 생성
		seller = createSeller();

		when(sellerRepository.save(any(Seller.class))).thenReturn(seller);
		sellerRepository.save(seller);

		//세션 생성
		session = new MockHttpSession();
		session.setAttribute(SessionConst.SESSION_KEY, "seller1@example.com");

		request.setSession(session);
	}

	@DisplayName("prehandle에서 ")
	@Nested
	class Prehandle{
		@DisplayName("세션에 이메일이 존재하면서, 이메일에 해당하는 seller를 조회할 수 있으면 true를 반환한다.")
		@Test
		void preHandleTestByValidEmail() throws Exception {

			given(request.getSession(true)).willReturn(session);
			given(sellerRepository.findByEmail(anyString())).willReturn(Optional.of(seller));


			assertThat(sellerInterceptor.preHandle(request, response, handler)).isTrue();
		}

		@DisplayName("세션에 이메일이 존재하지만, 이메일에 해당하는 seller를 조회할 수 없으면 false를 반환한다.")
		@Test
		void preHandleTestByInvalidEmail() throws Exception {
			given(request.getSession(true)).willReturn(session);
			given(sellerRepository.findByEmail(anyString())).willReturn(Optional.empty());

			assertThat(sellerInterceptor.preHandle(request, response, handler)).isFalse();
		}

		@DisplayName("세션이 존재하지 않으면 false를 반환한다.")
		@Test
		void preHandleTestByEmptyEmail() throws Exception{
			given(request.getSession(true)).willReturn(new MockHttpSession());
			given(sellerRepository.findByEmail(any())).willReturn(Optional.empty());

			assertThat(sellerInterceptor.preHandle(request, response, handler)).isFalse();
		}
	}

	@DisplayName("postHandle에서 SellerThreadLocal이 삭제되어 null을 반환한다.")
	@Test
	void postHandelClearConsumerThreadLocal() throws Exception{
		ConsumerThreadLocal.set(consumer);

		given(request.getSession(true)).willReturn(session);
		sellerInterceptor.postHandle(request,response,handler,modelAndView);

		assertThat(SellerThreadLocal.get()).isNull();
	}

	Seller createSeller(){
		return Seller.of(new SellerCreateRequestDto("seller1@example.com","1111","010-0000-0000","1234"));
	}
}
