package com.order.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.dto.Field;
import com.order.dto.OrderDto;
import com.order.dto.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProduct {

    private final KafkaTemplate kafkaTemplate;

    public Object send(String topic, Object orderDto) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(orderDto);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("kafka Product {}", orderDto);

        return orderDto;
    }


}
