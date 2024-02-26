package com.project.openmarket.global.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.project.openmarket.global.interceptor.ConsumerInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final ConsumerInterceptor consumerInterceptor;
	private static final String BASIC_URL = "/api/v1";
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry
			.addInterceptor(consumerInterceptor)
			.addPathPatterns(BASIC_URL+"/consumer/**");

	}
}