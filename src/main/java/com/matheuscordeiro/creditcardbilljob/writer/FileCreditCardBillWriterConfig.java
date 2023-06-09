package com.matheuscordeiro.creditcardbilljob.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.ResourceSuffixCreator;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.matheuscordeiro.creditcardbilljob.domain.CreditCardBill;

@Configuration
public class FileCreditCardBillWriterConfig {
	@Bean
	public MultiResourceItemWriter<CreditCardBill> filesCreditCardBill() {
		return new MultiResourceItemWriterBuilder<CreditCardBill>()
				.name("filesCreditCardBill")
				.resource(new FileSystemResource("files/credit-card-bill"))
				.itemCountLimitPerResource(1)
				.resourceSuffixCreator(suffixCreator())
				.delegate(fileCreditCardBill())
				.build();
	}

	private FlatFileItemWriter<CreditCardBill> fileCreditCardBill() {
		return new FlatFileItemWriterBuilder<CreditCardBill>()
				.name("fileCreditCardBill")
				.resource(new FileSystemResource("files/credit-card-bill.txt"))
				.lineAggregator(lineAggregator())
				.headerCallback(headerCallback())
				.footerCallback(footerCallback())
				.build();
	}

	private FlatFileFooterCallback footerCallback() {
		return new TotalTransactionFooterCallback();
	}

	private FlatFileHeaderCallback headerCallback() {
		return new FlatFileHeaderCallback() {
		
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.append(String.format("%121s\n", "Credit Card XPTO"));
				writer.append(String.format("%121s\n\n", "Street One, 1"));
			}
		};
	}

	private LineAggregator<CreditCardBill> lineAggregator() {
		return new LineAggregator<CreditCardBill>() {

			@Override
			public String aggregate(CreditCardBill creditCardBill) {
				StringBuilder writer = new StringBuilder();
				writer.append(String.format("Name: %s\n", creditCardBill.getCustomer().getName()));
				writer.append(String.format("Address: %s\n\n\n", creditCardBill.getCustomer().getAddress()));
				writer.append(String.format("Completed Credit Card bill %d\n", creditCardBill.getCreditCard().getCreditCardNumber()));
				writer.append("-------------------------------------------------------------------------------------------------------------------------\n");
				writer.append("DATE DESCRIPTION VALUE\n");
				writer.append("-------------------------------------------------------------------------------------------------------------------------\n");
				
				creditCardBill.getTransactions().forEach(transaction -> {
					writer.append(String.format("\n[%10s] %-80s - %s",
							new SimpleDateFormat("dd/MM/yyyy").format(transaction.getDate()),
							transaction.getDescription(),
							NumberFormat.getCurrencyInstance().format(transaction.getValue())));
				});
				return writer.toString();
			}
			
		};
	}

	private ResourceSuffixCreator suffixCreator() {
		return new ResourceSuffixCreator() {

			@Override
			public String getSuffix(int index) {
				return index + ".txt";
			}
		};
	}
}
