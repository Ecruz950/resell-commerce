package com.ecruz.resell_ecommerce.cart;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	public Cart() {
	}

	public Cart(Long userID, Long productId) {
		this.userId = userID;
		this.productId = productId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserID() {
		return userId;
	}

	public void setuserId(Long userId) {
		this.userId = userId;
	}

	public Long getproductId() {
		return productId;
	}

	public void setproductId(Long productId) {
		this.productId = productId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cart cart = (Cart) o;
		return Objects.equals(id, cart.id) && Objects.equals(userId, cart.userId) && Objects.equals(productId, cart.productId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userId, productId);
	}

	@Override
	public String toString() {
		return "Cart{" + "id=" + id + ", userId=" + userId + ", productId=" + productId + '}';
	}
}
