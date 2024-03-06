package com.project.openmarket.global.interceptor;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.entity.Seller;

@ExtendWith(MockitoExtension.class)
public abstract class InterceptorTestMock {
	protected MockHttpServletRequest request = mock(MockHttpServletRequest.class);;
	protected MockHttpServletResponse response = new MockHttpServletResponse();
	@Mock
	protected Object handler;
	@Mock
	protected ModelAndView modelAndView;
	@Mock
	protected Consumer consumer;
	@Mock
	protected Seller seller;
}
