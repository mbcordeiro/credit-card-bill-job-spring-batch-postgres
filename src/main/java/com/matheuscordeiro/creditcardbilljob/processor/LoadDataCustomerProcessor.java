package com.matheuscordeiro.creditcardbilljob.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.matheuscordeiro.creditcardbilljob.domain.CreditCardBill;
import com.matheuscordeiro.creditcardbilljob.domain.Customer;

@Component
public class LoadDataCustomerProcessor implements ItemProcessor<CreditCardBill, CreditCardBill>{

	private RestTemplate restTemplate = new RestTemplate();
	@Override
	public CreditCardBill process(CreditCardBill creditCardBill) throws Exception {
		final var uri = String.format("http://localhost:8081/profile/%d", creditCardBill.getCustomer().getId());
		final var response = restTemplate.getForEntity(uri, Customer.class);
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new ValidationException("Customer not found");
		}
		creditCardBill.setCustomer(response.getBody());
		return creditCardBill;
	}

}
