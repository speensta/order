package com.order.repository;

import com.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Iterable<OrderEntity> findOrderByUserId(String orderId);



}
