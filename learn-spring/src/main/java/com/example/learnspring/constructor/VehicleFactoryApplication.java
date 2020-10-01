package com.example.learnspring.constructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class VehicleFactoryApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(VehicleFactoryApplication.class, args);
    }

    @Autowired
    private FooService fooService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Constructor starts to run");
        fooService.run();
        log.info("Constructor completes");
    }
}
