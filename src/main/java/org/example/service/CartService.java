package org.example.service;

import org.example.Models.Cart;
import org.example.Models.Product;
import org.example.Models.User;
import org.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserService userService;
    public void addProductToCart(User user, Product product,Integer quantity){
        Cart item = this.cartRepository.findCartByUserAndProduct(user,product);
        if(item == null) {
            Cart cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(quantity);
            cart.setPrice(product.getPrice()*quantity);
            this.cartRepository.save(cart);
        }else{

            item.setQuantity(item.getQuantity()+quantity);
            item.setPrice(item.getPrice()+(product.getPrice()*quantity));

            this.cartRepository.save(item);
        }
    }

    public List<Cart> findAllCartItems()throws RuntimeException {

        Long userId = this.userService.getCurrentUserId();

        if(userId != null) {
            List<Cart> carts = this.cartRepository.findAllUserCarts(userId);

            if (carts != null) {
                return carts;
            }else{
                throw new RuntimeException("Cart je prazan.");
            }
        }
        throw new RuntimeException("Ne mogu pronaci trenutnoga usera.");
    }

    public void deleteCart(Long id){
        this.cartRepository.deleteById(id);
    }
}
