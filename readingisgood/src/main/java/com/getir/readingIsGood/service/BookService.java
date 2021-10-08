package com.getir.readingIsGood.service;

import com.getir.readingIsGood.exception.NotFoundException;
import com.getir.readingIsGood.exception.NotValidException;
import com.getir.readingIsGood.model.Book;
import com.getir.readingIsGood.model.Inventory;
import com.getir.readingIsGood.repository.BookRepository;
import com.getir.readingIsGood.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService
{
	private final InventoryRepository inventoryRepository;
	private final BookRepository bookRepository;

	public List<Book> getBooks()
	{
		return bookRepository.findAll();
	}

	public Book getBookById(String bookId)
	{
		return bookRepository.findById(bookId).orElse(null);
	}

	public Inventory getInventoryByBookId(String bookId)
	{
		return inventoryRepository.getBookInventory(bookId);
	}

	public Book createBook(Book book)
	{
		validateBook(book);
		return bookRepository.save(book);
	}

	public Inventory updateInventory(String bookId, int quantity)
	{
		Optional<Book> book = bookRepository.findById(bookId);
		if (book.isEmpty())
		{
			throw new NotFoundException("Book not found.");
		}

		if (quantity <= 0)
		{
			throw new NotValidException("Quantity must be greater than zero.");
		}

		Inventory inventory = inventoryRepository.getBookInventory(bookId);
		if (inventory == null)
		{
			inventory = new Inventory(bookId, quantity);
		}
		else
		{
			inventory.setQuantity(inventory.getQuantity() + quantity);
		}
		return inventoryRepository.save(inventory);
	}

	public List<Inventory> saveAllInventory(List<Inventory> stockList)
	{
		return inventoryRepository.saveAll(stockList);
	}

	public void validateBook(Book book)
	{
		if (book.getId() == null)
		{
			throw new NotValidException("Book id can not be null.");
		}
		else if (book.getName() == null || book.getName().trim().isEmpty())
		{
			throw new NotValidException("Book name can not be empty.");
		}
		else if (book.getPrice() == null)
		{
			throw new NotValidException("Book price can not be empty");
		}
		else if (book.getPrice() <= 0)
		{
			throw new NotValidException("Book price must be greater than zero");
		}
	}
}
