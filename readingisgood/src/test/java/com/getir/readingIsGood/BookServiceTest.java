package com.getir.readingIsGood;

import com.getir.readingIsGood.exception.NotFoundException;
import com.getir.readingIsGood.exception.NotValidException;
import com.getir.readingIsGood.model.Book;
import com.getir.readingIsGood.model.Inventory;
import com.getir.readingIsGood.repository.BookRepository;
import com.getir.readingIsGood.repository.InventoryRepository;
import com.getir.readingIsGood.service.BookService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest
{
    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private  InventoryRepository inventoryRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Book book;
    private Book book1;
    private List<Book> bookList;
    private Inventory inventory;

    @Before
    public void setUp()
    {
        book = new Book();
        book1 = new Book("100","Sherlock Holmes", 100.0D);
        bookList = new ArrayList<>();
        inventory = new Inventory();
    }

    @After
    public void tearDown()
    {
        this.bookService = null;
    }
    @Test(expected = Test.None.class)
    public void validateBook_success()
    {
        bookService.validateBook(book1);
    }

    @Test(expected = NotValidException.class)
    public void validateBook_failure()
    {
        bookService.validateBook(book);
    }

    @Test(expected = NotValidException.class)
    public void validateBook_failure_emptyBookName()
    {
        book.setId("33");
        book.setPrice(10.0D);
        bookService.validateBook(book);
    }

    @Test(expected = NotValidException.class)
    public void validateBook_failure_emptyBookPrice()
    {
        book.setId("33");
        book.setName("Sherlock Holmes");
        bookService.validateBook(book);
    }

    @Test(expected = NotValidException.class)
    public void validateBook_failure_negativeBookPrice()
    {
        book.setId("33");
        book.setName("Sherlock Holmes");
        book.setPrice(-10.0D);
        bookService.validateBook(book);
    }

    @Test
    public void getBooks_success()
    {
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> list = bookService.getBooks();
        Assert.assertNotNull(list);
    }

    @Test
    public void getBookById_success()
    {
        when(bookRepository.findById(anyString())).thenReturn(java.util.Optional.ofNullable(book));
        Book book = bookService.getBookById("Programming in C");
        Assert.assertNotNull(book);
    }

    @Test
    public void getInventoryByBookId_success()
    {
        when(inventoryRepository.getBookInventory(anyString())).thenReturn(inventory);
        Inventory inventory = bookService.getInventoryByBookId("Programming in C");
        Assert.assertNotNull(book);
    }

    @Test
    public void updateInventory_success()
    {
        when(bookRepository.findById(anyString())).thenReturn(java.util.Optional.ofNullable(book1));
        when(inventoryRepository.getBookInventory(anyString())).thenReturn(inventory);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);
        Inventory inventory = bookService.updateInventory("100", 50);
        Assert.assertNotNull(inventory);
    }

    @Test
    public void updateInventory_success2()
    {
        when(bookRepository.findById(anyString())).thenReturn(java.util.Optional.ofNullable(book1));
        when(inventoryRepository.getBookInventory(anyString())).thenReturn(null);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);
        Inventory inventory = bookService.updateInventory("100", 50);
        Assert.assertNotNull(inventory);
    }

    @Test(expected = NotFoundException.class)
    public void updateInventory_failure()
    {
        when(bookRepository.findById(anyString())).thenReturn(java.util.Optional.ofNullable(null));
        bookService.updateInventory("100", 50);

    }

    @Test
    public void saveAllInventory_success()
    {
        when(inventoryRepository.saveAll(new ArrayList<>())).thenReturn(new ArrayList<>());
        List<Inventory> list  = bookService.saveAllInventory(new ArrayList<>());
        Assert.assertNotNull(list);
    }

    @Test(expected = NotValidException.class)
    public void updateInventory_failure_invalidQuantity()
    {
        when(bookRepository.findById(anyString())).thenReturn(java.util.Optional.ofNullable(book1));
        bookService.updateInventory("100", -50);

    }

    @Test
    public void createBook_success()
    {
        when(bookRepository.save(any(Book.class))).thenReturn(book1);
        Book book = bookService.createBook(book1);
        Assert.assertNotNull(book);
    }

}
