package com.example.chatting.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping("/")
    public String home() {

        return "/platform/home";
    }

    @GetMapping("/chattings")
    public String chattings() {

        return "/platform/chats";
    }
}
