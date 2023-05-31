package com.matheuscordeiro.creditcardbilljob.reader;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

import com.matheuscordeiro.creditcardbilljob.domain.CreditCardBill;
import com.matheuscordeiro.creditcardbilljob.domain.Transaction;

public class CreditCardBillReader implements ItemStreamReader<CreditCardBill> {
	private ItemStreamReader<Transaction> delegate;
	private Transaction transactionCurrent;
	
	public CreditCardBillReader(ItemStreamReader<Transaction> delegate) {
		super();
		this.delegate = delegate;
	}
	
	@Override
	public CreditCardBill read() throws Exception {
		if (transactionCurrent != null) {
			transactionCurrent = delegate.read();
		}
		final var creditCardBill = new CreditCardBill();
		final var transaction = transactionCurrent;
		transactionCurrent = null;
		if (transaction != null) {
			creditCardBill.setCreditCard(transaction.getCreditCard());
			creditCardBill.setCustomer(transaction.getCreditCard().getCustomer());
			creditCardBill.getTransactions().add(transaction);
			while (isTransactionRelation(transaction)) {
				creditCardBill.getTransactions().add(transaction);
			}
		}
		return creditCardBill;
	}

	private boolean isTransactionRelation(Transaction transaction) throws Exception {
		return peek() != null && transaction.getCreditCard().getCreditCardNumber() == transactionCurrent.getCreditCard().getCreditCardNumber();
	}

	private Transaction peek() throws Exception {
		return transactionCurrent = delegate.read();
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		delegate.open(executionContext);
	}
	
	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		delegate.update(executionContext);
	}
	
	@Override
	public void close() throws ItemStreamException {
		delegate.close();
	}

}
