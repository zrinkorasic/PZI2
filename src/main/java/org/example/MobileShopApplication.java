package org.example;


import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.ApplicationRunner;


@SpringBootApplication
public class MobileShopApplication implements ApplicationRunner {


    public static void main(String[] args) {
        SpringApplication.run(MobileShopApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
