package com.example.inventorystockmanagement.service.serviceİmpl;

import com.example.inventorystockmanagement.entities.Product;
import com.example.inventorystockmanagement.repositories.ProductRepository;
import com.example.inventorystockmanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public void saveProduct(Product product) {
		this.productRepository.save(product);
	}

	@Override
	public Product getProductById(long id) {
		Optional<Product> optional = productRepository.findById(id);
		Product product = null;
		if (optional.isPresent()) {
			product = optional.get();
		} else {
			throw new RuntimeException(" Product not found for id :: " + id);
		}
		return product;
	}

	@Override
	public void deleteProductById(long id) {
		this.productRepository.deleteById(id);
	}
	
	

}
