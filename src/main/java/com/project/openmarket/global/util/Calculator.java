package com.project.openmarket.global.util;

public class Calculator {

	public static Long getPoint(Long amount){
		double percentage = 0.02;
		return Math.round(amount * percentage);
	}

	public static Long getRevenue(Long amount) {
		double commissionPercentage = 0.05;
		return amount - Math.round(amount * commissionPercentage);
	}
}
