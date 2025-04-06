package com.project.openmarket.domain.review.entity.enums;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Score {
	EMTPY("없음", -1),
	VERY_SATISFIED("매우 만족", 5),
	SATISFIED("만족", 4),
	NEITHER_SATISFIED_NOR_DISSATISFIED("보통", 3),
	DISSATISFIED("불만족", 2),
	VERY_DISSATISFIED("매우 불만족", 1);

	private String key;
	private int value;

	private Score(String key, int value) {
		this.key = key;
		this.value = value;
	}

	public static Score getScoreByValue(int value) {
		return Arrays.stream(values())
			.filter(v -> v.getValue() == value)
			.findFirst()
			.orElse(EMTPY);
	}

	public static Score fromString(String score) {
		for (Score s : Score.values()) {
			if (s.name().equalsIgnoreCase(score)) {
				return s;
			}
		}
		throw new IllegalArgumentException("Unknown enum value: " + score);
	}
}
