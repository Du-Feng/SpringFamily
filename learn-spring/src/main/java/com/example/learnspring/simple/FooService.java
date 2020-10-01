package com.example.learnspring.simple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FooService {
    @Autowired
    private com.example.learnspring.simple.FooFormatter fooFormatter;

    public void run() {
        log.info("This is FooService run method");
        fooFormatter.format();
    }
}
