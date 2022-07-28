package com.order.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProduct {

    private final KafkaTemplate kafkaTemplate;

    public Object send(String topic, OrderDto orderDto) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(this.getSchema(), this.getPayload(orderDto));

        try {
            jsonInString = mapper.writeValueAsString(kafkaOrderDto);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Order Product {}", orderDto);

        return orderDto;
    }

    public Schema getSchema() {

        List<Field> fields = Arrays.asList(
            new Field("string", true, "order_id"),
            new Field("string", true, "product_id"),
            new Field("int32", true, "qty"),
            new Field("int32", true, "unit_price"),
            new Field("int32", true, "total_price")
        );

        return Schema.builder().type("struct")
                .fields(fields)
                .optional(false)
                .name("orders")
                .build();

    }

    public Payload getPayload(OrderDto orderDto) {

        return Payload.builder().order_id(orderDto.getOrderId())
                .user_id(orderDto.getUserId())
                .product_id(orderDto.getProductId())
                .qty(orderDto.getQty())
                .unit_price(orderDto.getUnitPrice())
                .total_price(orderDto.getTotalPrice())
                .build();

    }


}
