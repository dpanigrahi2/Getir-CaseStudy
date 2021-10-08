package com.getir.readingIsGood.controller;

import com.getir.readingIsGood.exception.NotFoundException;
import com.getir.readingIsGood.exception.NotValidException;
import com.getir.readingIsGood.model.Book;
import com.getir.readingIsGood.model.Inventory;
import com.getir.readingIsGood.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
//@Api(tags = {"Book Details"},description = " </b><style>.models {display: none !important}</style>")

public class BookController
{
	private final BookService bookService;
	@GetMapping(produces = "application/json")
	@ApiOperation(value = "Find all books.")
	public ResponseEntity<Object> findBooks()
	{
		final var books = bookService.getBooks();

		if (CollectionUtils.isEmpty(books))
		{
			throw new NotFoundException("No book found.");
		}
		Map<String, Object> response = new HashMap<>();
		response.put("numberOfBooks", books.size());
		response.put("books", books);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	//@ApiOperation("Get book with given ID.")
	public ResponseEntity<Object> findBookById(@ApiParam(name = "id", type = "String", value = "Book id") @PathVariable("id") String id)
	{
		Book book = bookService.getBookById(id);
		if (book == null)
		{
			throw new NotFoundException("Book id: '" + id + "' not found.");
		}
		return new ResponseEntity<>(book, HttpStatus.OK);
	}


	@PostMapping()
	//@ApiOperation("Create book.")
	public ResponseEntity<Object> createBook(
			@ApiParam(name = "book", type = "Book", value = "Book to be created.", required = true) @RequestBody Book book)
	{
		if (book.getId() == null || book.getId().isEmpty())
		{
			throw new NotValidException("Book Id can not be empty");
		}
		else if (book.getName() == null || book.getName().isEmpty())
		{
			throw new NotValidException("Book name can not be empty");
		}
		else if(book.getPrice() == null || book.getPrice() < 0)
		{
			throw new NotValidException("Book price must be a non negative value");
		}
		Book newBook = bookService.createBook(book);
		log.info("Book created: {}", newBook.toString());
		return new ResponseEntity<>(newBook, HttpStatus.CREATED);
	}

	@PutMapping("/{id}/stocks/{quantity}")
	//@ApiOperation(value = "Update book's inventory")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> updateStock(
			@ApiParam(name = "id", type = "String") @PathVariable("id") String bookId,
			@ApiParam(name = "quantity",type = "int") @PathVariable("quantity") int qty)
	{
		Inventory inventory = bookService.updateInventory(bookId, qty);
		log.info("Inventory updated: {}", inventory.toString());
		return new ResponseEntity<>(inventory, HttpStatus.OK);
	}
}
