package org.example.Controllers;

import org.example.service.CartService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.example.Models.Cart;
import org.example.Models.Product;
import org.example.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @PostMapping("/addToCart/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String addToCart(@PathVariable Long id, @RequestParam int quantity, Model model) {
        try {
            User user = userService.findCurrentUser();
            Product product = productService.findById(id);
            cartService.addProductToCart(user, product, quantity);
            return "redirect:/allProducts";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage/errorPage";
        }
    }

    @GetMapping("/cartList")
    public String cartList(Model model)
    {
        try{
            List<Cart> carts = cartService.findAllCartItems();
            model.addAttribute("carts",carts);
            return "cart/cartProductList";
        }catch (RuntimeException e){
            model.addAttribute("error",e.getMessage());
            return "errorPage/errorPage";
        }

    }

    @PostMapping("/deleteCart/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String deleteProductFromCart(@PathVariable Long id){
        this.cartService.deleteCart(id);
        return "redirect:/cartList";
    }

}
