package com.example.inventorystockmanagement.service.serviceİmpl;

import com.example.inventorystockmanagement.entities.SoldProduct;
import com.example.inventorystockmanagement.repositories.SoldProductRepository;
import com.example.inventorystockmanagement.service.SoldProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SoldProductServiceImpl implements SoldProductService {

	@Autowired
	private SoldProductRepository soldProductRepository;

	@Override
	public List<SoldProduct> getAllSoldProducts() {
		return soldProductRepository.findAll();
	}

	@Override
	public void saveSoldProduct(SoldProduct soldProduct) {
		this.soldProductRepository.save(soldProduct);
	}

	@Override
	public SoldProduct getProductById(long id) {
		Optional<SoldProduct> optional = soldProductRepository.findById(id);
		SoldProduct soldProduct = null;
		if (optional.isPresent()) {
			soldProduct = optional.get();
		} else {
			throw new RuntimeException("SoldProduct not found for id : " + id);
		}
		return soldProduct;
	}

	@Override
	public void deleteSoldProductById(long id) {
		this.soldProductRepository.deleteById(id);
	}
	
	@Override
	public void deleteSoldProductAllById(List<Long> soldProductIds) {
		this.soldProductRepository.deleteAllById(soldProductIds);
	}

	@Override
	public void deleteSoldProductBeObject(SoldProduct soldProduct) {
		this.soldProductRepository.delete(soldProduct);
	}

	public void deleteWithRawSqlQuery(long sp_id) {
		soldProductRepository.deleteWithRawSqlQuery(sp_id);
	}
}
