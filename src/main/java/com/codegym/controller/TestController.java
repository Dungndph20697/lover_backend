package com.codegym.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/get")
    public String get() {
        return "test";
    }
}
