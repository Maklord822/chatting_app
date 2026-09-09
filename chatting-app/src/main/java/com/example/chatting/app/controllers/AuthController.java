package com.example.chatting.app.controllers;

import com.example.chatting.app.dtos.UserRequestDto;
import com.example.chatting.app.entities.User;
import com.example.chatting.app.repositories.UserRepository;
import com.example.chatting.app.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/user/get")
    public ResponseEntity<?> getUser(Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {

            User user = (User) authentication.getPrincipal();

            return ResponseEntity.ok(userService.getUserById(user.getId()));

        }
        return ResponseEntity.ok(false);
    }


    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(Authentication authentication,
                                          @RequestBody @Valid UserRequestDto userRequestDto,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        if (authentication != null && authentication.isAuthenticated()) {

            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(userService.registerUser(userRequestDto, request, response));

    }

    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(Authentication authentication,
                                       @RequestBody @Valid UserRequestDto userRequestDto,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {

        if (authentication != null && authentication.isAuthenticated()) {

            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(userService.loginUser(userRequestDto, request, response));

    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {

            User user = (User) authentication.getPrincipal();
            userRepository.delete(user);
            return ResponseEntity.ok("deleted");
        }

        return null;
    }

}
