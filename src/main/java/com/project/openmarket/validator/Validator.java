package com.project.openmarket.validator;

import java.util.regex.Pattern;

public class Validator {
	private static final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	private static final Pattern PHONENUMBER = Pattern.compile("^0\\d{1,2}(-|\\))\\d{3,4}-\\d{4}$");
	public static void validateEmail(final String email){
		if(!EMAIL.matcher(email).matches()){
			throw new IllegalArgumentException();
		}
	}

	public static void validatePhoneNumber(final String phoneNumber){
		if(!PHONENUMBER.matcher(phoneNumber).matches()){
			throw new IllegalArgumentException();
		}
	}
}
