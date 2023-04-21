package com.example.inventorystockmanagement.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "price")
	private int price;
	
	@Column(name = "stock")
	private int stock;

	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id",referencedColumnName = "id")
	List<SoldProduct> sold_products = new ArrayList<>();
	
	
	public Product() {
		
	}
	
	public Product(String name, int price, int stock) {
		super();
		this.name = name;
		this.price = price;
		this.stock = stock;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public List<SoldProduct> getSold_products() {
		return sold_products;
	}

	public void setSold_products(List<SoldProduct> sold_products) {
		this.sold_products = sold_products;
	}

}
