package com.getir.readingIsGood.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Document(value = "inventory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory
{
	@Id
	private String bookId;

	private int quantity;
}
