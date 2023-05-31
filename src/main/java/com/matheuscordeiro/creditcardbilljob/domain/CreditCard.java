package com.matheuscordeiro.creditcardbilljob.domain;

public class CreditCard {
	private int creditCardNumber;
	private Customer customer;
	
	public int getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(int creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
