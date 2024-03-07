package com.project.openmarket.global.exception;

import com.project.openmarket.global.exception.enums.ExceptionConstants;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class CustomException extends IllegalArgumentException {
	private final ExceptionConstants error;

	public CustomException(ExceptionConstants e) {
		super(e.getMessage());
		this.error = e;
		log.error("error code = {}, message = {}",e.getCode(), e.getMessage());

	}
}
