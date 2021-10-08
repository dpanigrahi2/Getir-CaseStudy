package com.getir.readingIsGood.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistics
{
	private String month;

	private int totalOrderCount;

	private int totalBookCount;

	private Double totalPurchasedAmount;
}
