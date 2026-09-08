package com.example.chatting.app.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
public class PathsConfig {

    @Bean(value = "publicPaths")
    public List<String> publicPaths() {

        return List.of("/");
    }

    @Bean(value = "securedPaths")
    public List<String> securedPaths() {

        return List.of();
    }

}
