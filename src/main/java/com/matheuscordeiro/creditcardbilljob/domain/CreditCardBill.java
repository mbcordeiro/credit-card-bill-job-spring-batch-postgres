package com.matheuscordeiro.creditcardbilljob.domain;

import java.util.ArrayList;
import java.util.List;

public class CreditCardBill {
	private Customer customer;
	private CreditCard creditCard;
	private List<Transaction> transactions = new ArrayList<>();
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	public Double getTotal() {
		return transactions.stream().mapToDouble(Transaction::getValue).reduce(0.0, Double::sum);
	}
	
}
