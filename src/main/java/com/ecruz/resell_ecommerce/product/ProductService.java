package com.ecruz.resell_ecommerce.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product createProduct(Product product) {
		return productRepository.save(product);
	}

	public Product updateProductPrice(Long id, Product product) {
		Product existingProduct = productRepository.findById(id).orElse(null);
		if (existingProduct != null) {
			existingProduct.setTitle(product.getTitle());
			existingProduct.setPrice(product.getPrice());
			existingProduct.setCategory(product.getCategory());
			existingProduct.setDescription(product.getDescription());
			existingProduct.setImage(product.getImage());
			existingProduct.setRating(product.getRating());
			return productRepository.save(existingProduct);
		}
		return null;
	}

	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
}
