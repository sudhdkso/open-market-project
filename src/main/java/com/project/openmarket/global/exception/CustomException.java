package com.project.openmarket.global.exception;

import com.project.openmarket.global.exception.enums.ExceptionConstants;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class CustomException extends IllegalArgumentException {
	private ExceptionConstants error;

	public CustomException(String message){
		super(message);
		log.error("message = {}",message);
	}

	public CustomException(ExceptionConstants e) {
		super(e.getMessage());
		log.error("error code = {}, message = {}",e.getCode(), e.getMessage());

	}
}
