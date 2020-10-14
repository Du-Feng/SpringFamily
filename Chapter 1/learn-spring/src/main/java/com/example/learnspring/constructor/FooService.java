package com.example.learnspring.constructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FooService {
    private FooFormatter fooFormatter;

    @Autowired
    public FooService(FooFormatter fooFormatter) {
        log.info("This is FooService Constructor");
        this.fooFormatter = fooFormatter;
    }

    public void run() {
        log.info("This is FooService run method");
        fooFormatter.format();
    }
}
