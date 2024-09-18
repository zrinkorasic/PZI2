package org.example.service;


import org.example.Models.Role;
import org.example.Models.User;
import org.example.Models.UserDetails;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void checkEmailAndLoadUser(User user)
    {
        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new IllegalArgumentException("User vec postoji.");
        }
        else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));

            Set<Role> roles = user.getRoles();
            if (roles == null || roles.isEmpty()) {
                roles = new HashSet<>();
                roles.add(Role.USER);
                user.setRoles(roles);
            }

            userRepository.save(user);
        }
    }
    public void adminRegistration(){

        User admin = new User();
        admin.setFirstName("admin123");
        admin.setLastName("admin123");
        admin.setEmail("admin123@gmail.com");
        admin.setPassword("admin123");

        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        admin.setRoles(roles);
        User existingAdmin = userRepository.findByEmail("admin123@gmail.com");
        if(existingAdmin == null){
            this.checkEmailAndLoadUser(admin);
        }else {
            System.out.println("Admin already exists");
        }

    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;

                return userDetails.getUser().getId();
            }
        }

        return null;
    }

    public User findCurrentUser()
    {
        if(this.getCurrentUserId() != null) {

            Optional<User> user = userRepository.findById(this.getCurrentUserId());

            if (user.isPresent()) {
                return user.get();
            } else {
                throw new RuntimeException("User ne postoji");
            }
        }
        throw new RuntimeException("Ne mozemo pronaci trenutnog usera");
    }
    public User findCurrentUserForAllProducts()
    {
        if (this.getCurrentUserId() != null) {
            Optional<User> user = userRepository.findById(this.getCurrentUserId());
            return user.orElse(null);
        }

        return null;
    }

}
