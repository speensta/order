package com.order.controller;

import com.order.dto.OrderDto;
import com.order.kafka.KafkaProduct;
import com.order.service.OrderService;
import com.order.vo.RequestOrder;
import com.order.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderRestController {

    private final OrderService orderService;

    private final ModelMapper modelMapper;

    private final KafkaProduct kafkaProduct;

    @PostMapping("/{userId}/order")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder requestOrder) {

        OrderDto orderDto = modelMapper.map(requestOrder, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto resultOrderDto =orderService.createOrder(orderDto);

        ResponseOrder responseOrder = modelMapper.map(resultOrderDto, ResponseOrder.class);

        kafkaProduct.send("test-topic", orderDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);

    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> findByUserId(@PathVariable("userId") String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findOrderByUserId(userId).stream()
                .map(m -> modelMapper.map(m, ResponseOrder.class)).collect(Collectors.toList()));
    }


}
