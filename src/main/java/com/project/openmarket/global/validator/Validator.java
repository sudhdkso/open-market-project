package com.project.openmarket.global.validator;

import java.util.regex.Pattern;

public class Validator {
	private static final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	private static final Pattern PHONENUMBER = Pattern.compile("^0\\d{1,2}(-|\\))\\d{3,4}-\\d{4}$");
	private Validator(){

	}

	public static boolean validateEmail(final String email){
		return !(email == null || email.isBlank() || !EMAIL.matcher(email).matches());

	}

	public static boolean validatePhoneNumber(final String phoneNumber){
		return PHONENUMBER.matcher(phoneNumber).matches();
	}
}
