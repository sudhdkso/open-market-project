package com.project.openmarket.global.interceptor;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpSession;

import com.project.openmarket.domain.user.dto.request.ConsumerCreateReqestDto;
import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.repository.ConsumerRepository;
import com.project.openmarket.global.auth.ConsumerThreadLocal;
import com.project.openmarket.global.auth.enums.SessionConst;

public class ConsumerInterceptorTestsTest extends InterceptorTestMock {
	@InjectMocks
	private ConsumerInterceptor consumerInterceptor;
	@Mock
	private ConsumerRepository consumerRepository;

	private static MockHttpSession session;
	@BeforeEach
	void setUp() {
		//고객 생성
		consumer = createConsumer();

		when(consumerRepository.save(any(Consumer.class))).thenReturn(consumer);
		consumerRepository.save(consumer);

		//세션 생성
		session = new MockHttpSession();
		session.setAttribute(SessionConst.SESSION_KEY, "asdf1@example.com");

		request.setSession(session);
	}

	@DisplayName("prehandle에서 ")
	@Nested
	class Prehandle{
		@DisplayName("세션에 이메일이 존재하면서, 이메일에 해당하는 consumer를 조회할 수 있으면 true를 반환한다.")
		@Test
		void preHandleTestByValidEmail() throws Exception {

			when(request.getSession(true)).thenReturn(session);
			when(consumerRepository.findByEmail(anyString())).thenReturn(Optional.of(consumer));


			assertThat(consumerInterceptor.preHandle(request, response, handler)).isTrue();
		}

		@DisplayName("세션에 이메일이 존재하지만, 이메일에 해당하는 consumer를 조회할 수 없으면 false를 반환한다.")
		@Test
		void preHandleTestByInvalidEmail() throws Exception {
			when(request.getSession(true)).thenReturn(session);
			when(consumerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

			assertThat(consumerInterceptor.preHandle(request, response, handler)).isFalse();
		}

		@DisplayName("세션에 이메일이 존재하지 않으면 false를 반환한다.")
		@Test
		void preHandleTestByEmptyEmail() throws Exception{
			when(request.getSession(true)).thenReturn(new MockHttpSession());
			when(consumerRepository.findByEmail(any())).thenReturn(Optional.empty());

			assertThat(consumerInterceptor.preHandle(request, response, handler)).isFalse();
		}
	}

	@DisplayName("postHandle에서 ConsumerThreadLocal이 삭제되어 null을 반환한다.")
	@Test
	void test() throws Exception{
		ConsumerThreadLocal.set(consumer);

		when(request.getSession(true)).thenReturn(session);
		consumerInterceptor.postHandle(request,response,handler,modelAndView);

		assertThat(ConsumerThreadLocal.get()).isNull();
	}

	Consumer createConsumer(){
		return Consumer.of(new ConsumerCreateReqestDto("asdf1@example.com","1111","010-0000-0000","1234","dd"));
	}
}
