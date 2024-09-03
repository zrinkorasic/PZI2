package org.example.Controllers;

import org.example.service.CartService;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.example.Models.OrderItem;
import org.example.Models.Orders;
import org.example.Models.User;
import org.example.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class OrderController {

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    UserService userService;


    @GetMapping("/checkoutItems")
    @PreAuthorize("hasAuthority('USER')")
    public String checkoutForm(Model model)
    {
        model.addAttribute("orders",new Orders());
        return "order/createOrderForm";
    }

    @PostMapping("/checkoutOrder")
    @PreAuthorize("hasAuthority('USER')")
    public String checkoutOrder(Model model, @Valid Orders orders, BindingResult result)
    {
        if(result.hasErrors()){
            model.addAttribute("orders",orders);
            return "order/createOrderForm";
        }else{
            try{
                this.orderService.saveOrder(orders);
                return "redirect:/allProducts";
            }catch (RuntimeException e){
                model.addAttribute("error",e.getMessage());
                return"errorPage/errorPage";
            }
        }
    }

    @GetMapping("/private/allOrders")
    public String allOrders(Model model){

        List<Orders>orders = orderRepository.findAll();
        model.addAttribute("orders",orders);

        return "order/allOrders";
    }
    @PostMapping("/private/deleteOrder/{orderId}")
    public String deleteOrder(Model model, @PathVariable Long orderId){

        this.orderService.deleteOrder(orderId,model);

        return "redirect:/private/allOrders";
    }

    @GetMapping("/private/oderDetails/{orderId}")
    public String orderDetail(Model model,@PathVariable Long orderId){

        List<OrderItem>orderItems = orderService.getOrderDetails(orderId,model);
        if (orderItems != null) {
            model.addAttribute("orderItems", orderItems);
            return "order/allOrderItems";
        } else {
            model.addAttribute("error", "Error");
            return "errorPage/errorPage";
        }

    }
}
