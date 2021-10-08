package com.getir.readingIsGood.model;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Document(value = "order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetails
{
    private String userId;

    private List<OrderEntity> orderDetails;

    private LocalDateTime orderPlacedOn;

    private Double costOfOrder;

}
