package org.example.repository;

import org.example.Models.OrderItem;
import org.example.Models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    @Query("SELECT oi FROM OrderItem oi INNER JOIN oi.orders o WHERE o = :order")
    public List<OrderItem> findAllItemsForOrder(Orders order);
}
