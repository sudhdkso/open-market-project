package com.project.openmarket.global.interceptor;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.project.openmarket.domain.user.entity.Seller;
import com.project.openmarket.domain.user.repository.SellerRepository;
import com.project.openmarket.global.auth.SellerThreadLocal;
import com.project.openmarket.global.auth.enums.SessionConst;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SellerInterceptor implements HandlerInterceptor {

	private final SellerRepository sellerRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(true);

		String email = (String) session.getAttribute(SessionConst.SESSION_KEY);

		Optional<Seller> seller = sellerRepository.findByEmail(email);

		if (!seller.isPresent()) {
			return false;
		}

		SellerThreadLocal.set(seller.get());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView) throws Exception {
		SellerThreadLocal.remove();
	}
}
