package com.project.openmarket.domain.review.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {
	private final MongoTemplate mongoTemplate;

	@Override
	public double calculateAvgScoreByProductId(ObjectId id) {
		MatchOperation match = Aggregation.match(Criteria.where("product").is(id));
		GroupOperation group = Aggregation.group("product").avg("rating").as("avgRating");

		Aggregation aggregation = Aggregation.newAggregation(match, group);

		AggregationResults<AvgRatingResult> results = mongoTemplate.aggregate(aggregation, "review",
			AvgRatingResult.class);

		AvgRatingResult result = results.getUniqueMappedResult();
		return result != null ? result.avgRating() : 0.0;
	}

	private record AvgRatingResult(ObjectId id, double avgRating) {
	}
}
