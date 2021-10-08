package com.getir.readingIsGood.repository;

import com.getir.readingIsGood.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String>
{
}
