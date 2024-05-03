package com.project.openmarket.global.util.Converter;

import com.project.openmarket.domain.review.entity.enums.Score;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ScoreConverter implements AttributeConverter<Score, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Score score) {
		return score.getValue();
	}

	@Override
	public Score convertToEntityAttribute(Integer value) {
		if (value == null) {
			return null;
		}

		for (Score score : Score.values()) {
			if (score.getValue() == value) {
				return score;
			}
		}

		throw new IllegalArgumentException("Unknown code: " + value);
	}
}