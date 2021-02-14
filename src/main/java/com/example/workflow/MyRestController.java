package com.example.workflow;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {

    private static int count=0;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        count++;
        if(count<3) {

            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("heps");

        }
        return ResponseEntity.ok("hello");
    }
}
