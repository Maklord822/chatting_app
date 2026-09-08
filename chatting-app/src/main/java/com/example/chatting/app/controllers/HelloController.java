package com.example.chatting.app.controllers;

import com.example.chatting.app.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {


        return "hello world";
    }

    @GetMapping("/home")
    public ResponseEntity<?> home(
            @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            return ResponseEntity.ok(
                   false
            );
        }

        return ResponseEntity.ok(
              user.getId()
        );
    }
}
