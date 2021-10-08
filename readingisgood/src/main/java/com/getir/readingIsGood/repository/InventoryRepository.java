package com.getir.readingIsGood.repository;

import com.getir.readingIsGood.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface InventoryRepository extends MongoRepository<Inventory, String>
{
	@Query("{'bookId' : ?0}")
	Inventory getBookInventory(String bookId);
}
