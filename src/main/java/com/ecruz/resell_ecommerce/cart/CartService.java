package com.ecruz.resell_ecommerce.cart;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

	private final CartRepository cartRepository;

	@Autowired
	public CartService(CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}

	public List<Cart> getCartByUserId(Long userId) {
		// fetch all rows with the same userId
		return cartRepository.findAll()
				.stream()
				.filter(cart -> cart.getUserID().equals(userId))
				.toList();
	}

	public void addProductToCart(Cart cart) {
		cartRepository.save(cart);
	}

	@Transactional
	public void removeProductFromCart(Long userId, Long productId) {
		cartRepository.deleteByUserIdAndProductId(userId, productId);
	}
}
