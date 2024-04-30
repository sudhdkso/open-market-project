package com.project.openmarket.global.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculatorTest {

	@Test
	@DisplayName("포인트 계산 Calculator 테스트")
	void testCalculatePoint() {
		long amount = 1000L;
		long expectedPoint= 20L; // 1000 * 0.02

		long actualPoint = Calculator.getPoint(amount);

		assertEquals(expectedPoint, actualPoint);
	}

	@Test
	@DisplayName("수익 계산 Calculator 테스트")
	void testCalculateRevenue() {
		long amount = 1000L;
		long expectedRevenue= 950L; // 1000 - (1000 * 0.05)

		long actualRevenue = Calculator.getRevenue(amount);

		assertEquals(expectedRevenue, actualRevenue);
	}
}