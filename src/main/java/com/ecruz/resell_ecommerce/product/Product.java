package com.ecruz.resell_ecommerce.product;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "title")
	private String title;

	@Column(name = "price")
	private double price;

	@Column(name = "description", length = 1000)
	private String description;

	@Column(name = "category")
	private String category;

	@Column(name = "image_url")
	private String image;

	@Column(name = "rating")
	private Rating rating;

	public Product( String title, double price, String description, String category, String image, Rating rating) {
		this.title = title;
		this.price = price;
		this.description = description;
		this.category = category;
		this.image = image;
		this.rating = rating;
	}

	public Product() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return id == product.id && Double.compare(price, product.price) == 0 && Objects.equals(title, product.title) && Objects.equals(description, product.description) && Objects.equals(category, product.category) && Objects.equals(image, product.image) && Objects.equals(rating, product.rating);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, price, description, category, image, rating);
	}

	@Override
	public String toString() {
		return "Product{" + "id=" + id + ", title='" + title + '\'' + ", price=" + price + ", description='" + description + '\'' + ", category='" + category + '\'' + ", imageURL='" + image + '\'' + ", rating=" + rating + '}';
	}
}
