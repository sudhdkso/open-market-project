package com.project.openmarket.global.exception;

import com.project.openmarket.global.exception.enums.ExceptionConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
	private final String code;
	private final String message;

	public CustomException(ExceptionConstants e) {
		super(e.getMessage());
		this.code = e.getCode();
		this.message = e.getMessage();
		log.error("error code = {}, message = {}",e.getCode(), e.getMessage());

	}
}
