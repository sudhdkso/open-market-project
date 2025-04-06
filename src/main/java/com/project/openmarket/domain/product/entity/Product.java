package com.project.openmarket.domain.product.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.project.openmarket.domain.base.entity.BaseTime;
import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.dto.request.ProductUpdateReqeustDto;
import com.project.openmarket.domain.user.entity.Seller;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document(collection = "products")
public class Product extends BaseTime {
	@Id
	private ObjectId id;

	private String name;

	private String description;

	private int price;

	private int stock;

	private double avgRating;

	@DocumentReference
	private Seller seller;
	//TODO: 2024.05.10 구매수 컬럼 추가하기

	@Builder
	public Product(String name, String description, int price, int stock, Seller seller) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.seller = seller;
		this.avgRating = 0.0;
	}

	public static Product of(ProductRequestDto dto, Seller seller) {
		return new Product(dto.name(), dto.description(), dto.price(), dto.stock(), seller);
	}

	public void update(ProductUpdateReqeustDto dto) {
		if (dto.name() != null) {
			this.name = dto.name();
		}
		this.price = dto.price();
		this.stock = dto.stock();
	}

	public void updateAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}

	public void increaseStock(int count) {
		this.stock += count;
	}

	public void decreaseStock(int count) {
		this.stock -= count;
	}

	public boolean canBuy(int count) {
		return this.stock >= count;
	}

	public boolean isSoldOut() {
		return this.stock <= 0;
	}

	public boolean isSameName(String another) {
		return this.name.equals(another);
	}

	public ObjectId getSellerId() {
		return this.seller.getId();
	}
}
