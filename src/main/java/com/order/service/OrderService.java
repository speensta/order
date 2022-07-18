package com.order.service;

import com.order.dto.OrderDto;
import com.order.entity.OrderEntity;
import com.order.repository.OrderRepository;
import com.order.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;

    public List<OrderDto> findAllCatalog() {
        return orderRepository.findAll().stream()
                .map(o -> modelMapper.map(o, OrderDto.class)).collect(Collectors.toList());
    }

    public List<OrderDto> findOrderByUserId(String userId) {
        List<OrderDto> list = new ArrayList<>();
        orderRepository.findOrderByUserId(userId).forEach(o -> {
                    OrderDto orderDto = modelMapper.map(o, OrderDto.class);
                    orderDto.setTotalPrice(o.getUnitPrice()*o.getQty());
                    list.add(orderDto);
                });
        return list;
    }

    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        OrderEntity orderEntity = modelMapper.map(orderDto, OrderEntity.class);
        orderRepository.save(orderEntity);
        return modelMapper.map(orderEntity, OrderDto.class);
    }



}
