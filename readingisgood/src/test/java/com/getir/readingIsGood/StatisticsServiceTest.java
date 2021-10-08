package com.getir.readingIsGood;

import com.getir.readingIsGood.exception.NotFoundException;
import com.getir.readingIsGood.model.Customer;
import com.getir.readingIsGood.model.Order;
import com.getir.readingIsGood.model.OrderEntity;
import com.getir.readingIsGood.repository.CustomerRepository;
import com.getir.readingIsGood.repository.InventoryRepository;
import com.getir.readingIsGood.repository.OrderRepository;
import com.getir.readingIsGood.service.BookService;
import com.getir.readingIsGood.service.StatisticsService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceTest
{
    @InjectMocks
    private BookService bookService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private  InventoryRepository inventoryRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Mock
    private CustomerRepository customerRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
        this.statisticsService = null;
    }

    @Test(expected = NotFoundException.class)
    public void getStatistics_failure()
    {
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(null);
        statisticsService.getStatistics("Debasish@test.com");

    }

    @Test(expected = Test.None.class)
    public void getStatistics_success()
    {
        OrderEntity orderEntity = new OrderEntity("100", 10);
        Order order = new Order("Debasish@test.com", new ArrayList<>(Arrays.asList(orderEntity)), LocalDateTime.now(), null);
        List<Order> orderList = new ArrayList<>(Arrays.asList(order));

        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(new Customer());
        when(orderRepository.getAllOrdersByDate(any(),any(),any())).thenReturn(new ArrayList<>(orderList));
        statisticsService.getStatistics("Debasish@test.com");

    }

}
