package org.example.Controllers;

import org.example.service.BrandService;
import org.example.service.UserService;
import org.example.Models.Brand;
import org.example.Models.User;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class BrandController {

    private final BrandService brandService;

    private final UserService userService;

    public BrandController(BrandService brandService, UserService userService) {
        this.brandService = brandService;
        this.userService = userService;
    }

    @GetMapping("/private/createBrand")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showBrandForm(Model model){
        model.addAttribute("brand",new Brand());
        return "brand/createBrand";
    }
    @PostMapping("/private/createBrand")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createBrand(@Valid Brand brand, BindingResult result, Model model){

        if(result.hasErrors()){
            model.addAttribute("brand",brand);
            return "brand/createBrand";
        }
        this.brandService.save(brand);
        return "redirect:/private/createBrand";
    }

    @GetMapping("/private/allBrands")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String allBrands(Model model){

        User user = this.userService.findCurrentUser();
        model.addAttribute("currentUser", user.isAdmin());
        model.addAttribute("brands",this.brandService.findAllBrands());
        return "brand/showBrands";
    }

    @PostMapping("/private/deleteBrand/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteBrand(@PathVariable Long id){
        this.brandService.delete(id);

        return "redirect:/private/allBrands";
    }

    @PostMapping("/private/updateBrand/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateBrandForm(Model model ,@PathVariable Long id)
    {
        Optional<Brand> optionalBrand = this.brandService.findById(id);
        if(optionalBrand.isPresent()){
            Brand brand = optionalBrand.get();
            model.addAttribute("brand",brand);
            return "brand/updateBrand";
        }

        return "redirect:/private/allBrands";
    }

    @PostMapping("/private/updateBrand")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateBrand(@Valid Brand brand,BindingResult result,Model model)
    {
        if(result.hasErrors()){
            model.addAttribute("brand",brand);
            return "brand/updateBrand";
        }
        this.brandService.save(brand);
        return "redirect:/private/allBrands";
    }
}
