package com.getir.readingIsGood.repository;

import com.getir.readingIsGood.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CustomerRepository extends MongoRepository<Customer, String>
{
	@Query(value = "{'_id' : ?0}")
	Customer findCustomerByEmail(String email);
}
