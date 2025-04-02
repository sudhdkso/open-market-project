package com.project.openmarket.global.exception;

public record ErrorResponse(
	String code,
	String message
) {
	@Override
	public String toString() {
		return "ErrorResponse{" +
			"code='" + code + '\'' +
			", message='" + message + '\'' +
			'}';
	}
}
