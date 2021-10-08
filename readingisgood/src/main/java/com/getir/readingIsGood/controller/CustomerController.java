package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.exception.NotFoundException;
import com.getir.readingIsGood.model.Customer;
import com.getir.readingIsGood.service.CustomerService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

	private final CustomerService customerService;

	@GetMapping
	//@ApiOperation("Find all customers.")
	public ResponseEntity<Object> findCustomers()
	{
		List<Customer> customers = customerService.getCustomers();

		if (customers == null || customers.isEmpty())
		{
			throw new NotFoundException("No customers found");
		}

		Map<String, Object> response = new HashMap<>();
		response.put("customers", customers);
		response.put("numberOfCustomers", customers.size());
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/{id}")
	//@ApiOperation("Find customer details by email id.")
	public ResponseEntity<Object> findCustomerById(
				@ApiParam(name = "id", type = "String")
				@PathVariable("id") String id)
	{
		Customer customer = customerService.getCustomerById(id);
		if (customer == null) 
		{
			throw new NotFoundException("Customer id '" + id + "' does not exist");
		}
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}

	@PostMapping
	//@ApiOperation("Create customer.")
	public ResponseEntity<Object> createNewCustomer(
				@ApiParam(name = "customer", type = "Customer", required = true)
				@RequestBody Customer customer)
	{
		Customer newCustomer = customerService.createCustomer(customer);
		log.info("Customer added: {}", newCustomer);
		return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
	}

	@GetMapping("/{id}/orders")
	//@ApiOperation("Find the given customer's orders.")
	public ResponseEntity<Object> findCustomerOrders(
				@ApiParam(name = "id", type = "String") @PathVariable("id") String id,
				@ApiParam(name = "page", type = "int") @RequestParam(defaultValue = "0") int page,
				@ApiParam(name = "size", type = "int") @RequestParam(defaultValue = "10") int size)
	{

		Map<String, Object> orders = customerService.getCustomerOrders(id, page, size);
		if (orders == null || orders.isEmpty())
		{
			throw new NotFoundException("No orders found");
		}
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
}
