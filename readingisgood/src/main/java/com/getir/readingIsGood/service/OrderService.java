package com.getir.readingIsGood.service;

import com.getir.readingIsGood.exception.NotFoundException;
import com.getir.readingIsGood.exception.NotValidException;
import com.getir.readingIsGood.model.Book;
import com.getir.readingIsGood.model.Customer;
import com.getir.readingIsGood.model.Inventory;
import com.getir.readingIsGood.model.Order;
import com.getir.readingIsGood.model.OrderEntity;
import com.getir.readingIsGood.repository.CustomerRepository;
import com.getir.readingIsGood.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class OrderService 
{
	private final OrderRepository orderRepository;
	private final CustomerRepository customerRepository;
	private final BookService bookService;

	public Map<String, Object> getOrdersByDateBetween(LocalDateTime fromDateTime, LocalDateTime toDateTime, int pageNumber, int size)
	{
		fromDateTime = calculateFromDate(fromDateTime);
		toDateTime = calculateToDate(toDateTime);

		if (fromDateTime.isAfter(LocalDateTime.now()))
		{
			throw new NotValidException("From date should be less than or equal to current date");
		}

		if (pageNumber < 0)
		{
			throw new NotValidException("Page number can not be negative");
		}
		Page<Order> orderPages = orderRepository.getOrdersByDate(fromDateTime, toDateTime, PageRequest.of(pageNumber, size));
		return createResponse(orderPages);
	}

	public Map<String, Object> getOrdersByOrderedBy(Customer customer, int pageNumber, int size)
	{
		Page<Order> orderPages = orderRepository.getOrderById(customer.getEmail(), PageRequest.of(pageNumber, size));
		return createResponse(orderPages);
	}

	public Order createOrder(Order order)
	{
		if (order.getUserId() == null || order.getUserId().isEmpty())
		{
			throw new NotFoundException("CustomerId can not be empty.");
		}

		String customerId = order.getUserId();
		Customer customer = customerRepository.findCustomerByEmail(customerId);
		if (customer == null)
		{
			throw new NotFoundException("Customer id: '" + customerId + "' not found.");
		}

		if (CollectionUtils.isEmpty(order.getEntries()))
		{
			throw new NotValidException("Order can not be null");
		}

		List<Inventory> stockList = new ArrayList<>();
		double costofOrder = 0.0D;
		for (OrderEntity entry : order.getEntries())
		{
			String bookId = entry.getBookId();
			Book book = bookService.getBookById(bookId);
			if (book == null) 
			{
				throw new NotFoundException("Book id: '" + bookId + "' not found.");
			}
			entry.setBookId(book.getId());

			if (entry.getQuantity() <= 0) 
			{
				throw new NotValidException("Quantity must be greater than zero.");
			}

			Inventory inventory = bookService.getInventoryByBookId(bookId);
			if (inventory.getQuantity() < entry.getQuantity()) 
			{
				throw new NotFoundException("Not enough inventory for book id: '" + bookId + "'.");
			}
			inventory.setQuantity(inventory.getQuantity() - entry.getQuantity());
			stockList.add(inventory);
			costofOrder += book.getPrice() * entry.getQuantity();

		}
		bookService.saveAllInventory(stockList);
		order.setUserId(customer.getEmail() + RandomStringUtils.random(4, true, true));
		order.setCostOfOrder(costofOrder);
		return orderRepository.save(order);
	}


	public List<Order> getOrderById(String orderId)
	{
		List<Order> list = orderRepository.getOrderById(orderId);
		for (Order order : list)
		{
			order.setUserId(order.getUserId().substring(0,order.getUserId().length() - 4));
		}
		return list;
	}


	private LocalDateTime calculateFromDate(LocalDateTime fromDateTime)
	{
		if (fromDateTime == null) 
		{
			return LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0);
		} 
		return fromDateTime;
	}

	private LocalDateTime calculateToDate(LocalDateTime toDateTime)
	{
		if (toDateTime == null)
		{
			return LocalDateTime.now();
		}
		return toDateTime;
	}

	public Map<String, Object> createResponse(Page<Order> orderPages)
	{
		for (Order order : orderPages)
		{
			order.setUserId(order.getUserId().substring(0,order.getUserId().length() - 4));
		}
		Map<String, Object> response = new HashMap<>();
		response.put("orders", orderPages.getContent());
		response.put("currentPage", orderPages.getNumber());
		response.put("totalItems", orderPages.getTotalElements());
		response.put("totalPages", orderPages.getTotalPages());
		return response;
	}
}
