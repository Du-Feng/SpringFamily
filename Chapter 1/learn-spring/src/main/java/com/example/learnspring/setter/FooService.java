package com.example.learnspring.setter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FooService {
    private FooFormatter fooFormatter;

    @Autowired
    public void setFooFormatter(FooFormatter fooFormatter) {
        log.info("This is FooService setFooFormatter method");
        this.fooFormatter = fooFormatter;
    }

    public void run() {
        log.info("This is FooService run method");
        fooFormatter.format();
    }
}
