package org.example.Controllers;

import org.example.service.UserService;
import org.example.Models.User;
import org.example.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @GetMapping("/auth/register")
    public String registerForm(Model model)
    {
        User user = new User();
        model.addAttribute("user",user);

        return "users/registerUser";
    }

    @PostMapping("/auth/register")
    public String registerUser(@Valid User user, Model model, BindingResult result)
    {
        if(result.hasErrors()){
            model.addAttribute("user",user);
            return "users/registerUser";
        }else{
            try{
                this.userService.checkEmailAndLoadUser(user);
                return "redirect:/auth/login";
            }catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
                return "users/registerUser";
            }
        }
    }
    @GetMapping("/auth/login")
    public String login ( Model model) {
        User user = new User();
        model.addAttribute("user",user);
        return "users/login";
    }
    @GetMapping("/private/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String listUsers(Model model){
        List<User> users = this.userRepository.findAll();

        model.addAttribute("users",users);
        return "users/index";
    }

    @GetMapping("/private/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addUserForm(Model model){
        User user = this.userService.findCurrentUser();

        model.addAttribute("user",new User());
        model.addAttribute("currentUser",user.isAdmin());

        return"users/add";
    }

    @PostMapping("/private/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addUser(@Valid User user,BindingResult result,Model model)
    {
        if(result.hasErrors()){
            model.addAttribute("user",user);
            return "users/add";
        }else {
            try{
                this.userService.checkEmailAndLoadUser(user);
                return "redirect:/private/users";
            }catch (IllegalArgumentException e){
                model.addAttribute("error",e.getMessage());
                return "users/add";
            }
        }
    }

    @PostMapping("/private/users/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@PathVariable Long userId) {
        this.userRepository.deleteById(userId);
        return "redirect:/private/users";
    }

    @GetMapping("/private/users/edit/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showEditUserForm(@PathVariable Long userId, Model model) {
        try{
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Neispravan ID korisnika: " + userId));
            model.addAttribute("user", user);
        }catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "errorPage/errorPage";
        }
        return "users/edit";
    }

    @PostMapping("/private/users/edit/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateUser(@PathVariable Long userId,
                             @Valid @ModelAttribute User user,
                             BindingResult result,
                             Model model) {
        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "users/edit";
        }else{
            User existingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Neispravan ID korisnika: " + userId));

            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = encoder.encode(user.getPassword());
            existingUser.setPassword(password);
            existingUser.setRoles(user.getRoles());

            userRepository.save(existingUser);
            return "redirect:/private/users";
        }
    }
}