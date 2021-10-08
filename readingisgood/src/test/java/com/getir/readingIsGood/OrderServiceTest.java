package com.getir.readingIsGood;

import com.getir.readingIsGood.exception.NotFoundException;
import com.getir.readingIsGood.exception.NotValidException;
import com.getir.readingIsGood.model.Customer;
import com.getir.readingIsGood.model.Inventory;
import com.getir.readingIsGood.model.Order;
import com.getir.readingIsGood.repository.BookRepository;
import com.getir.readingIsGood.repository.CustomerRepository;
import com.getir.readingIsGood.repository.OrderRepository;
import com.getir.readingIsGood.service.BookService;
import com.getir.readingIsGood.service.CustomerService;
import com.getir.readingIsGood.service.OrderService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest
{
    @InjectMocks
    private OrderService orderService;

    @Mock
    private CustomerService customerService;

    @Mock
    private BookService bookService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private  OrderRepository orderRepository;

    @Mock
    private BookRepository bookRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Customer customer;

    private Inventory inventory;

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
        this.orderService = null;
    }

    @Test(expected = NotValidException.class)
    public void getOrdersByDateBetween_failure_inPageNumber()
    {
        orderService.getOrdersByDateBetween(null, null, -1,5);
        customerService.validateCustomer(customer);
    }

    @Test(expected = NotValidException.class)
    public void getOrdersByDateBetween_failure_fromDateGreaterThanCurrentDate()
    {
        LocalDateTime fromDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0, 0);
        orderService.getOrdersByDateBetween(fromDateTime, null, 0,5);
    }

    @Test
    public void getOrdersByDateBetween_success()
    {
        LocalDateTime fromDateTime = LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0);
        LocalDateTime toDateTime = LocalDateTime.of(2021, 2, 1, 0, 0, 0, 0);
        Page<Order> response = Mockito.mock(Page.class);
        when(orderRepository.getOrdersByDate(any(), any(),any())).thenReturn(response);
        Map<String, Object> map = orderService.getOrdersByDateBetween(fromDateTime, toDateTime, 0,5);
        Assert.assertNotNull(map);
    }

    @Test
    public void getOrderById_success()
    {
        Order order = new Order("Debasish@test.com", null, null, null);
        List<Order> orderList = new ArrayList<>(Arrays.asList(order));
        when(orderRepository.getOrderById(anyString())).thenReturn(orderList);
        List<Order> list = orderService.getOrderById("Debasish@test.com");
        Assert.assertNotNull(list);
    }

    @Test(expected = NotFoundException.class)
    public void createOrder_invalidOrder()
    {
        Order order = new Order("", null, null, null);
        orderService.createOrder(order);
    }

    @Test(expected = NotFoundException.class)
    public void createOrder_invalidCustomer()
    {
        Order order = new Order("Debasish@test.com", null, null, null);
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(null);
        orderService.createOrder(order);
    }

    @Test(expected = NotValidException.class)
    public void createOrder_failure_invalidOrder()
    {
        Order order = new Order("Debasish@test.com", new ArrayList<>(), null, null);
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(new Customer());
        orderService.createOrder(order);
    }

}
