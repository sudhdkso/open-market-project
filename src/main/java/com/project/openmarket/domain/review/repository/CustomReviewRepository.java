package com.project.openmarket.domain.review.repository;

import org.bson.types.ObjectId;

public interface CustomReviewRepository {

	double calculateAvgScoreByProductId(ObjectId productId);
}
