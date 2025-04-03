package com.project.openmarket.global.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.MappedInterceptor;

import com.project.openmarket.global.interceptor.ConsumerInterceptor;
import com.project.openmarket.global.interceptor.RoleBasedInterceptor;
import com.project.openmarket.global.interceptor.SellerInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final ConsumerInterceptor consumerInterceptor;
	private final SellerInterceptor sellerInterceptor;
	private final RoleBasedInterceptor roleBasedInterceptor;

	private static final String BASIC_URL = "/api/v1";

	@Bean
	public MappedInterceptor addSellerInterceptors() {
		return new MappedInterceptor(new String[]{BASIC_URL+"/seller/**"}, sellerInterceptor);
	}

	@Bean
	public MappedInterceptor addConsumerInterceptors() {
		return new MappedInterceptor(new String[]{ BASIC_URL+"/consumer/**"}, consumerInterceptor);
	}

	@Bean MappedInterceptor addRoleBasedInterceptors() {
		return new MappedInterceptor(new String[] {BASIC_URL+"/user"}, roleBasedInterceptor);
	}
}