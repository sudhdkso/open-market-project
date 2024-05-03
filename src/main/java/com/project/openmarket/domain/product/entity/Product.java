package com.project.openmarket.domain.product.entity;

import com.project.openmarket.domain.base.entity.BaseTime;
import com.project.openmarket.domain.product.dto.request.ProductRequestDto;
import com.project.openmarket.domain.product.dto.request.ProductUpdateReqeustDto;
import com.project.openmarket.domain.user.entity.Seller;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "products")
public class Product extends BaseTime {
	@Id
	@Column(name = "id", unique = true, nullable = false, updatable = false, columnDefinition = "BIGINT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "price")
	private int price;

	@Column(name = "stock")
	private int stock;

	@Column(name = "avg_score")
	private double avgScore;

	@ManyToOne
	@JoinColumn(name = "seller_id")
	private Seller seller;

	private Product(String name, int price, int stock, Seller seller){
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.seller = seller;
		this.avgScore = 0.0;
	}

	public static Product of(ProductRequestDto dto, Seller seller){
		return new Product(dto.name(), dto.price(), dto.stock(), seller);
	}

	public void update(ProductUpdateReqeustDto dto){
		this.name = dto.name();
		this.price = dto.price();
		this.stock = dto.stock();
	}

	public void updateAvgScore(double avgScore){
		this.avgScore = avgScore;
	}

	public void increaseStock(int count){
		this.stock += count;
	}

	public void decreaseStock(int count){
		this.stock -= count;
	}

	public boolean canBuy(int count){
		return this.stock >= count;
	}

	public boolean isSoldOut(){
		return this.stock <= 0;
	}

	public boolean isSameName(String another){
		return this.name.equals(another);
	}

}
