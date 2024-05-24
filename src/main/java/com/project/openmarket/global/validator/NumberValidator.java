package com.project.openmarket.global.validator;

import com.project.openmarket.domain.review.entity.enums.Score;

public class NumberValidator {
	private NumberValidator(){

	}
	public static boolean isScoreWithinRange(int score){
		return score >= Score.VERY_DISSATISFIED.getValue() && score <= Score.VERY_SATISFIED.getValue();
	}

	public static boolean isPositive(int number){
		return number > 0;
	}
}
