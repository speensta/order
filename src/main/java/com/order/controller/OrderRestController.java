package com.order.controller;

import com.order.dto.OrderDto;
import com.order.service.OrderService;
import com.order.vo.RequestOrder;
import com.order.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderRestController {

    private final OrderService orderService;

    private final ModelMapper modelMapper;

    @PostMapping("/{userId}/order")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder requestOrder) {

        OrderDto orderDto = modelMapper.map(requestOrder, OrderDto.class);
        orderDto.setUserId(userId);
        orderService.createOrder(orderDto);

        ResponseOrder responseOrder = modelMapper.map(orderDto, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseOrder);

    }

    @GetMapping("/{userId}/order")
    public ResponseEntity<List<ResponseOrder>> findByUserId(@PathVariable("userId") String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findOrderByUserId(userId).stream()
                .map(m -> modelMapper.map(m, ResponseOrder.class)).collect(Collectors.toList()));
    }


}
