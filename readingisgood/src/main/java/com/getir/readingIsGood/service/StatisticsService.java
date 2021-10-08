package com.getir.readingIsGood.service;

import com.getir.readingIsGood.exception.NotFoundException;
import com.getir.readingIsGood.model.Customer;
import com.getir.readingIsGood.model.Order;
import com.getir.readingIsGood.model.OrderEntity;
import com.getir.readingIsGood.model.Statistics;
import com.getir.readingIsGood.repository.CustomerRepository;
import com.getir.readingIsGood.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService
{
	private final OrderRepository orderRepository;
	private final CustomerRepository customerRepository;

	public Collection<Statistics> getStatistics(String customerId)
	{
		System.out.println("DEBASISH 111");
		Customer customer = customerRepository.findCustomerByEmail(customerId);
		if (customer == null)
		{
			throw new NotFoundException("Customer id: '" + customerId + "' not found.");
		}
		LocalDateTime fromDateTime = LocalDateTime.now().plus(1, ChronoUnit.MONTHS).minus(1, ChronoUnit.YEARS);
		LocalDateTime fromMonthStart = LocalDateTime.of(fromDateTime.getYear(), fromDateTime.getMonthValue(), 1, 0, 0, 0, 0);
		List<Order> orders = orderRepository.getAllOrdersByDate(customer.getEmail(), fromMonthStart, LocalDateTime.now());
		return groupOrders(orders);
	}

	private Collection<Statistics> groupOrders(List<Order> orders)
	{
    	Map<Month, Statistics> groupedOrders = new EnumMap<>(Month.class);
		orders.forEach(order -> {
			Month month = order.getDate().getMonth();
			if (groupedOrders.get(month) == null)
			{
				Statistics statistics = new Statistics(
						String.valueOf(Month.of(month.getValue())),
						1,
						order.getEntries().stream().map(OrderEntity::getQuantity).reduce(0, Integer::sum),
						order.getCostOfOrder());

				groupedOrders.put(month, statistics);
			} 
			else 
			{
				Statistics statistics = groupedOrders.get(month);
				statistics.setTotalOrderCount(statistics.getTotalOrderCount() + 1);
				statistics.setTotalBookCount(statistics.getTotalBookCount() + order.getEntries().stream().map(OrderEntity::getQuantity).reduce(0, Integer::sum));
				statistics.setTotalPurchasedAmount(statistics.getTotalPurchasedAmount() + (order.getCostOfOrder()));
				groupedOrders.put(month, statistics);
			}

		});
		return groupedOrders.values();
	}
}