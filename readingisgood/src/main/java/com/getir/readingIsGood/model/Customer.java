package com.getir.readingIsGood.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer
{
	@Id
	private String email;

	private String name;
}
