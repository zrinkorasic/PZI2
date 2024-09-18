package org.example.Controllers;

import org.example.service.BrandService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.example.Models.Product;
import org.example.Models.User;
import org.example.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    ProductRepository productRepository;
    private BrandService brandService;
    private ProductService productService;
    private UserService userService;

    public ProductController(BrandService brandService,
                             ProductService productService,
                             UserService userService) {
        this.brandService = brandService;
        this.productService = productService;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/private/showProductForm")
    public String addProduct(Model model)
    {
        Product product = new Product();
        model.addAttribute("product",product);
        model.addAttribute("brands", this.brandService.findAllBrands());
        return "product/createProduct";
    }

    @PostMapping("/private/addProduct")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addProduct(Model model, @Valid Product product, BindingResult result, @RequestParam("image") MultipartFile image)
    {
        if(result.hasErrors()){
            model.addAttribute("product",product);
            model.addAttribute("brands", this.brandService.findAllBrands());
            return "product/createProduct";
        }else{
            try {
                this.productService.saveProduct(product,image);
                return "redirect:/allProducts";
            } catch (IOException e) {
                model.addAttribute("error",e.getMessage());
                return "errorPage/errorPage";
            }

        }
    }
    @GetMapping("/allProducts")
    @PreAuthorize("hasAuthority('USER')")
    public String showProducts(Model model,
                               @RequestParam(name="search",required = false)String search) {

        try{
            List<Product> products = this.productService.findAll(search);
            User user = this.userService.findCurrentUserForAllProducts();
            model.addAttribute("products",products);
            if(user != null){
                model.addAttribute("currentUser",user.isAdmin());
                model.addAttribute("checkUser",true);
            }else{
                model.addAttribute("currentUser",false);
                model.addAttribute("checkUser",false);
            }
            model.addAttribute("brands",this.brandService.findAllBrands());
            return "product/showProducts";
        }catch(RuntimeException e){
            model.addAttribute("error",e.getMessage());
            return "errorPage/errorPage";
        }

    }

    @PostMapping("/private/deleteProduct/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteProduct(@PathVariable Long id,Model model)
    {
        try{
            this.productService.deleteProductById(id);
            return "redirect:/allProducts";
        }catch(RuntimeException e){
            model.addAttribute("error",e.getMessage());
            return "errorPage/errorPage";
        }

    }

    @GetMapping("/private/showProductForUpdate/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateProduct(@PathVariable Long id,Model model)
    {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            model.addAttribute("product",product);
            model.addAttribute("brands",this.brandService.findAllBrands());
            return "product/updateProduct";
        }else {
            return "redirect:/allProducts";
        }

    }

    @PostMapping("/private/updateProduct/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateProduct(Product product,@RequestParam("image")MultipartFile image,
                                @PathVariable Long productId,
                                Model model)
    {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Neispravan ID produckta: " + productId));
        try {
            this.productService.updateProduct(product,image,existingProduct);
            return "redirect:/allProducts";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage/errorPage";
        }
    }
    @GetMapping("/brandProduct/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String brandProducts(Model model, @PathVariable Long id){
        model.addAttribute("products",this.productService.findAllProductsByBrandId(id));
        return "product/brandProducts";
    }
}
