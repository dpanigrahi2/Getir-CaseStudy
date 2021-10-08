package com.getir.readingIsGood.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Document(value = "book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book
{
	@Id
	private String id;

	private String name;

	private Double price;
}
