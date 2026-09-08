package com.example.chatting.app.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
public class PathsConfig {

    @Bean(value = "publicPaths")
    public List<String> publicPaths() {

        return List.of("/",
               // "/home",
                "/csrf-token",
                "/auth/user/get",
                "/auth/user/register",
                "/auth/user/login",
                "/logout");
    }

    @Bean(value = "securedPaths")
    public List<String> securedPaths() {

        return List.of("/hello");
    }

}
