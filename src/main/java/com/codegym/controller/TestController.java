package com.codegym.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {

    //dụng
    @GetMapping("/get")
    public String get() {
        return "test";
    }

//    hoang
}
