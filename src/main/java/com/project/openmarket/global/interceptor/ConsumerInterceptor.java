package com.project.openmarket.global.interceptor;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.project.openmarket.domain.user.entity.Consumer;
import com.project.openmarket.domain.user.repository.ConsumerRepository;
import com.project.openmarket.domain.auth.ConsumerThreadLocal;
import com.project.openmarket.domain.auth.enums.SessionConst;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ConsumerInterceptor implements HandlerInterceptor {
	private final ConsumerRepository consumerRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(true);

		String email = (String) session.getAttribute(SessionConst.SESSION_KEY);

		Optional<Consumer> consumer = consumerRepository.findByEmail(email);

		if (!consumer.isPresent()) {
			return false;
		}

		ConsumerThreadLocal.set(consumer.get());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView) throws Exception {
		ConsumerThreadLocal.remove();
	}
}
