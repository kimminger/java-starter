package com.elderbyte.starter.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class ServletTestResource {

    @GetMapping
    public String hello(){
        return "Hello world (mvc)!";
    }


    @GetMapping("/fail")
    public String getAlwaysAuthFail(){
        throw new IllegalArgumentException(
                "This resource will always fail!",
                new IllegalStateException("I am a nested err.")
        );
    }

}
