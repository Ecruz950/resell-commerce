package com.ecruz.resell_ecommerce.product;

import jakarta.persistence.Embeddable;

@Embeddable
public class Rating {
	private double rate;
	private int count;

	// Getters and Setters
	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Rating{" + "rate=" + rate + ", count=" + count + '}';
	}
}
