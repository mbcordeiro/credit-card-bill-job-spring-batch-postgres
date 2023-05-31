package com.matheuscordeiro.creditcardbilljob.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.matheuscordeiro.creditcardbilljob.domain.CreditCardBill;
import com.matheuscordeiro.creditcardbilljob.domain.Transaction;
import com.matheuscordeiro.creditcardbilljob.reader.CreditCardBillReader;

@Configuration
public class CreditCardBillStepConfig {
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step creditCardBillStep(ItemStreamReader<Transaction> readTransactionItemReader,
			ItemProcessor<CreditCardBill, CreditCardBill> creditCardBillItemProcessor,
			ItemWriter<CreditCardBill> creditCardBillItemWriter) {
		return stepBuilderFactory.get("creditCardBillStep").<CreditCardBill, CreditCardBill>chunk(1)
				.reader(new CreditCardBillReader(readTransactionItemReader))
				.processor(creditCardBillItemProcessor)
				.writer(creditCardBillItemWriter)
				.build();
	}
}
