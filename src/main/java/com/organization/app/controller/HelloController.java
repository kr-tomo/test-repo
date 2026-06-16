package com.organization.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Hello", description = "Hello World API")
@RestController
public class HelloController {

    @Operation(summary = "Greet the user", description = "Returns a simple hello message")
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
