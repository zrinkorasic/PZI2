package org.example;


import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.ApplicationRunner;


@SpringBootApplication
public class MobileShopApplication implements ApplicationRunner {
    @Autowired
    private UserService userService;


    public static void main(String[] args) {
        SpringApplication.run(MobileShopApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userService.adminRegistration();

    }
}
