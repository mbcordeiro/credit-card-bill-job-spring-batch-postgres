package com.matheuscordeiro.creditcardbilljob.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.matheuscordeiro.creditcardbilljob.domain.CreditCardBill;

@Configuration
public class CreditCardBillStepConfig {
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step creditCardBillStep(ItemReader<CreditCardBill> creditCardBillItemReader,
			ItemProcessor<CreditCardBill, CreditCardBill> creditCardBillItemProcessor,
			ItemWriter<CreditCardBill> creditCardBillItemWriter) {
		return stepBuilderFactory.get("creditCardBillStep").<CreditCardBill, CreditCardBill>chunk(1)
				.reader(creditCardBillItemReader)
				.writer(creditCardBillItemWriter)
				.processor(creditCardBillItemProcessor)
				.build();
	}
}
