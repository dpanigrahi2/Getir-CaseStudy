package com.getir.readingIsGood.model;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.annotation.Id;
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
public class Order
{
    @Id
    private String userId;

    private List<OrderEntity> entries;

    private LocalDateTime date;

    @Hidden
    private Double costOfOrder;

}
