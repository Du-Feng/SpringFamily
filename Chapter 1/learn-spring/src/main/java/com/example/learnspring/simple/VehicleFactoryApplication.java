package com.example.learnspring.simple;

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
    private com.example.learnspring.simple.FooService fooService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Simple starts to run");
        fooService.run();
        log.info("Simple completes");
    }
}
