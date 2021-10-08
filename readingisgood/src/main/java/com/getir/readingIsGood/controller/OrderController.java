package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.exception.NotFoundException;
import com.getir.readingIsGood.model.Order;
import com.getir.readingIsGood.model.OrderDetails;
import com.getir.readingIsGood.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController
{

	private final OrderService orderService;

	@GetMapping
	//@ApiOperation(value = "Find orders by given date interval")
	public ResponseEntity<Map<String, Object>> findOrdersByDateBetween(
			@ApiParam(name = "from", value= "From Date", example = "2021-10-04T08:30:32.619") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
			@ApiParam(name = "to", value= "To Date", example = "2021-10-06T01:42:53.231") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
			@ApiParam(name = "page", type = "int") @RequestParam(defaultValue = "0") int pageNumber,
			@ApiParam(name = "size", type = "int") @RequestParam(defaultValue = "5") int size)
	{

		Map<String, Object> orders = orderService.getOrdersByDateBetween(fromDate, toDate, pageNumber, size);
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	//@ApiOperation("Find order by given userId")
	public ResponseEntity<Object> findOrderById(@ApiParam(name = "id", type = "String") @PathVariable("id") String id) 
	{
		List<Order> order = orderService.getOrderById(id);
		if (order == null)
		{
			throw new NotFoundException("No orders found for '" + id + "'");
		}
		return new ResponseEntity<>(order, HttpStatus.OK);
	}


	@PostMapping
	//@ApiOperation("Create order")
	public ResponseEntity<Object> createOrder(@ApiParam(name = "order", type = "Order", required = true) @RequestBody Order order) 
	{
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setUserId(order.getUserId());
		Order newOrder = orderService.createOrder(order);
		log.info("Order created: {}", newOrder);
		orderDetails.setOrderPlacedOn(newOrder.getDate());
		orderDetails.setCostOfOrder(newOrder.getCostOfOrder());
		orderDetails.setOrderDetails(newOrder.getEntries());
		return new ResponseEntity<>(orderDetails, HttpStatus.CREATED);
	}

}
