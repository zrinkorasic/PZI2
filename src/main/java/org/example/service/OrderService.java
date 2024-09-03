package org.example.service;

import org.example.Models.Cart;
import  org.example.Models.Orders;
import org.example.Models.OrderItem;
import org.example.Models.User;
import org.example.repository.OrderItemRepository;
import org.example.repository.OrderRepository;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserService userService;

    @Autowired
    org.example.service.CartService cartService;

    @Autowired
    OrderItemRepository orderItemRepository;
    public void saveOrder(Orders orders)throws RuntimeException{

        List<Cart> carts = this.cartService.findAllCartItems();
        User user = this.userService.findCurrentUser();
        if(carts.isEmpty()){
            throw new RuntimeException("Cart je prazan");
        }
        float totalPrice = 0;

        for(Cart item : carts){
            totalPrice = totalPrice+item.getPrice();
        }

        orders.setTotalPrice(totalPrice);
        orders.setUser(user);
        this.orderRepository.save(orders);

        for(Cart item : carts){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(orders);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());

            this.orderItemRepository.save(orderItem);
            this.cartService.deleteCart(item.getId());
        }
    }

    public void deleteOrder(Long orderId, Model model){
        try{
            Orders order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Neispravan ID ordera: " + orderId));

            List<OrderItem>orderItems = this.orderItemRepository.findAllItemsForOrder(order);

            for(OrderItem orderItem : orderItems ){
                this.orderItemRepository.deleteById(orderItem.getId());
            }
            this.orderRepository.deleteById(order.getId());
        }catch(IllegalArgumentException e){
            model.addAttribute("deleteOrderError", e.getMessage());
        }
    }

    public List<OrderItem> getOrderDetails(Long orderId,Model model){

        try{
            Orders order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Neispravan ID ordera: " + orderId));

            List<OrderItem> items = this.orderItemRepository.findAllItemsForOrder(order);
            return items;
        }catch(IllegalArgumentException e){
            model.addAttribute("error",e.getMessage());
            return null;
        }

    }
}
