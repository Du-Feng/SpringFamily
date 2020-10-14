package com.example.learnspring.simple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("fooFormatter")
@Slf4j
public class FooFormatter {
    public void format() {
        log.info("This is FooFormatter format methods");
    }
}
