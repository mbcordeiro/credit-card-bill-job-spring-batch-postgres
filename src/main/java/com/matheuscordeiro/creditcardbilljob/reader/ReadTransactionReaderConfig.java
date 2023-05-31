package com.matheuscordeiro.creditcardbilljob.reader;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.matheuscordeiro.creditcardbilljob.domain.CreditCard;
import com.matheuscordeiro.creditcardbilljob.domain.Customer;
import com.matheuscordeiro.creditcardbilljob.domain.Transaction;

@Configuration
public class ReadTransactionReaderConfig {
	@Bean
	public JdbcCursorItemReader<Transaction> readTransaction(@Qualifier("appDataSource") DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<Transaction>()
				.name("readTransaction")
				.dataSource(dataSource)
				.sql("select * from transaction join credit_card using(number_credit_card) order by number_credit_card")
				.rowMapper(rowMapperTransaction())
				.build();
	}

	private RowMapper<Transaction> rowMapperTransaction() {
		return new RowMapper<Transaction>() {
			@Override
			public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
				final var creditCard = new CreditCard();
				creditCard.setCreditCardNumber(rs.getInt("number_credit_card"));
				final var customer = new Customer();
				customer.setId(rs.getInt("customer"));
				creditCard.setCustomer(customer);
				final var transaction = new Transaction();
				transaction.setId(rs.getInt("id"));
				transaction.setCreditCard(creditCard);
				transaction.setDate(rs.getDate("date"));
				transaction.setValue(rs.getDouble("value"));
				transaction.setDescription(rs.getString("description"));
				return transaction;
			}
		};
	}
}
