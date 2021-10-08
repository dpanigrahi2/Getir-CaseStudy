package com.getir.readingIsGood;

import com.getir.readingIsGood.exception.NotFoundException;
import com.getir.readingIsGood.exception.NotValidException;
import com.getir.readingIsGood.model.Customer;
import com.getir.readingIsGood.model.Inventory;
import com.getir.readingIsGood.repository.CustomerRepository;
import com.getir.readingIsGood.repository.InventoryRepository;
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
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest
{
    @InjectMocks
    private CustomerService customerService;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private  InventoryRepository inventoryRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Customer customer;

    private Inventory inventory;

    @Before
    public void setUp()
    {
        customer = new Customer("Debasish@test.com","Debasish" );
    }

    @After
    public void tearDown()
    {
        this.customerService = null;
    }
    @Test(expected = Test.None.class)
    public void validateCustomer_success()
    {
        customerService.validateCustomer(customer);
    }

    @Test(expected = NotValidException.class)
    public void validateCustomer_failure_noEmail()
    {
        customer.setEmail("");
        customerService.validateCustomer(customer);
    }

    @Test(expected = NotValidException.class)
    public void validateCustomer_failure_inValidEmail()
    {
        customer.setEmail("Debasish");
        customerService.validateCustomer(customer);
    }

    @Test(expected = NotValidException.class)
    public void validateCustomer_failure_noName()
    {
        customer.setName("");
        customerService.validateCustomer(customer);
    }

    @Test(expected = NotValidException.class)
    public void validateCustomer_failure_invalidName()
    {
        customer.setName("Debasish1234");
        customerService.validateCustomer(customer);
    }


    @Test
    public void createCustomer_success()
    {
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(null);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        Customer newCustomer = customerService.createCustomer(customer);
        Assert.assertNotNull(newCustomer);
    }

    @Test(expected = NotValidException.class)
    public void createCustomer_failure_userEmailAlreadyExists()
    {
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(customer);
        Customer newCustomer = customerService.createCustomer(customer);
    }

    @Test
    public void getCustomerById_success()
    {
        when(customerRepository.findById(anyString())).thenReturn(java.util.Optional.ofNullable(customer));
        Customer newCustomer = customerService.getCustomerById("Debasish@test.com");
        Assert.assertNotNull(newCustomer);
    }

    @Test
    public void getCustomers_success()
    {
        when(customerRepository.findAll()).thenReturn(new ArrayList<>());
        List<Customer> customerList = customerService.getCustomers();
        Assert.assertNotNull(customerList);
    }

    @Test(expected = NotFoundException.class)
    public void getCustomerOrders_failure_customerNotFound()
    {
        when(customerRepository.findById(anyString())).thenReturn(java.util.Optional.ofNullable(null));
        customerService.getCustomerOrders("Debasish@test.com",0,5);
    }

    @Test(expected = NotValidException.class)
    public void getCustomerOrders_failure_invalidPageSize()
    {
        when(customerRepository.findById(anyString())).thenReturn(java.util.Optional.ofNullable(customer));
        customerService.getCustomerOrders("Debasish@test.com",-5,5);
    }
}
