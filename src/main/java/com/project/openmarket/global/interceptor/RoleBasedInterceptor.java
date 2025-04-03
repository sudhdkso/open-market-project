package com.project.openmarket.global.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class RoleBasedInterceptor implements HandlerInterceptor {
	private final SellerInterceptor sellerInterceptor;
	private final ConsumerInterceptor consumerInterceptor;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "세션 없음");
			return false;
		}

		String role = (String) session.getAttribute("role"); // 세션에서 역할 가져오기
		if ("seller".equals(role)) {
			sellerInterceptor.preHandle(request, response, handler);

		} else if ("consumer".equals(role)) {
			consumerInterceptor.preHandle(request, response, handler);
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한 없음");
			return false;
		}

		return true;
	}
}
