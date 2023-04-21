package com.example.inventorystockmanagement.service;

import com.example.inventorystockmanagement.entities.Product;

import java.util.List;


public interface ProductService {
	List<Product> getAllProducts();
	void saveProduct(Product product);
	Product getProductById(long id);
	void deleteProductById(long id);
}
