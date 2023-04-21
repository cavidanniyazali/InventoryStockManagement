package com.example.inventorystockmanagement.service;

import com.example.inventorystockmanagement.entities.SoldProduct;

import java.util.List;

public interface SoldProductService {
	List<SoldProduct> getAllSoldProducts();
	void saveSoldProduct(SoldProduct soldProduct);
	SoldProduct getProductById(long id);
	void deleteSoldProductById(long id);
	void deleteSoldProductAllById(List<Long> soldProductIds);
	void deleteSoldProductBeObject(SoldProduct soldProduct);
	 void deleteWithRawSqlQuery(long sp_id);
}
