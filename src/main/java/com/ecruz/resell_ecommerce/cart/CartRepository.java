package com.ecruz.resell_ecommerce.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	void deleteByUserIdAndProductId(Long userId, Long productId);
}
