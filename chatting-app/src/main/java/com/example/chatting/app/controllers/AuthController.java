package com.example.chatting.app.controllers;

import com.example.chatting.app.dtos.UserRequestDto;
import com.example.chatting.app.entities.User;
import com.example.chatting.app.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/user/get")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal User user) {

        if (user==null) {
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(userService.getUserById(user.getId()));

    }


    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@AuthenticationPrincipal User user,
                                          @RequestBody @Valid UserRequestDto userRequestDto) {

        if (user!=null) {
            return ResponseEntity.ok(false);
        }

       return ResponseEntity.ok(userService.registerUser(userRequestDto));

    }

}
