package com.getir.readingIsGood.repository;

import com.getir.readingIsGood.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String>
{
	@Query("{'_id' : {	$regex : /^?0/ } }")
	Page<Order> getOrderById(String customerId, Pageable pageable);

	@Query("{'date' : { $gte: ?0, $lte: ?1 } }")
	Page<Order> getOrdersByDate(LocalDateTime fromDateTime, LocalDateTime toDateTime, Pageable pageable);

	@Query("{'_id' : {	$regex : /^?0/ }, 'date' : { $gte: ?1, $lte: ?2 } }")
	List<Order> getAllOrdersByDate(String emailId, LocalDateTime fromDateTime, LocalDateTime toDateTime);

	@Query("{'_id' : {	$regex : /^?0/ } }")
	List<Order> getOrderById(String userId);
}
