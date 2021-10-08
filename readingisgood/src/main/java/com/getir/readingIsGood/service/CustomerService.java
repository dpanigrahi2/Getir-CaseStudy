package com.getir.readingIsGood.service;

import com.getir.readingIsGood.exception.NotFoundException;
import com.getir.readingIsGood.exception.NotValidException;
import com.getir.readingIsGood.model.Customer;
import com.getir.readingIsGood.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerService
{
	private final CustomerRepository customerRepository;
	private final OrderService orderService;

	public List<Customer> getCustomers()
	{
		return customerRepository.findAll();
	}

	public Customer getCustomerById(String customerId)
	{
		return customerRepository.findById(customerId).orElse(null);
	}

	public Customer createCustomer(Customer customer)
	{
		validateCustomer(customer);
		if (customerRepository.findCustomerByEmail(customer.getEmail()) != null)
		{
			throw new NotValidException("Customer with email id '"+ customer.getEmail()+ "' already exists.");
		}
		else
		{
			return customerRepository.save(customer);
		}
	}

	public Map<String, Object> getCustomerOrders(String customerId, int pageSize, int size)
	{
		Customer customer = getCustomerById(customerId);
		if (customer == null)
		{
			throw new NotFoundException("Customer id: '" + customerId + "' not found.");
		}
		if (pageSize < 0)
		{
			throw new NotValidException("Page number can not be negative");
		}

		return orderService.getOrdersByOrderedBy(customer, pageSize, size);
	}

	public void validateCustomer(Customer customer)
	{
		String name = "^[a-zA-Z\\s]+";
		if (StringUtils.isEmpty(customer.getEmail()))
		{
			throw new NotValidException("Email address can not be empty");
		}
		else if (!customer.getEmail().contains("@"))
		{
			throw new NotValidException("Email address is not valid");
		}
		else if (StringUtils.isEmpty(customer.getName()))
		{
			throw new NotValidException("Customer name can not be empty");
		}
		else if (!customer.getName().matches(name))
		{
			throw new NotValidException("Customer name should contain only alphabets and spaces");
		}
	}

}
