package com.example.chatting.app.services;

import com.example.chatting.app.dtos.UserDto;
import com.example.chatting.app.dtos.UserRequestDto;
import com.example.chatting.app.dtos.UserResponseDto;
import com.example.chatting.app.entities.User;
import com.example.chatting.app.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenBasedRememberMeServices rememberMeServices;

    public UserResponseDto registerUser(UserRequestDto userRequestDto, HttpServletRequest request, HttpServletResponse response) {

        if (userRepository.existsByName(userRequestDto.name())) {
            return new UserResponseDto(false,"name exists", null);
        }

        User user = new User();
        user.setName(userRequestDto.name());
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));
        user.setChats(List.of());

        userRepository.save(user);

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDto.name(), userRequestDto.password())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        rememberMeServices.loginSuccess(request, response, auth);

        return new UserResponseDto(true, "registered", transformUserToDto(user));
    }

    public UserResponseDto loginUser(UserRequestDto userRequestDto, HttpServletRequest request, HttpServletResponse response) {

        if (!userRepository.existsByName(userRequestDto.name())) {
            return new UserResponseDto(false,"name doesn't exist", null);
        }

        User user = userRepository.findByName(userRequestDto.name()).orElseThrow();

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDto.name(), userRequestDto.password())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        rememberMeServices.loginSuccess(request, response, auth);

        return new UserResponseDto(true, "logged in", transformUserToDto(user));
    }

    public UserDto getUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow();

        return transformUserToDto(user);

    }

  public UserDto transformUserToDto(User user) {

      return new UserDto(
              user.getId(),
              user.getName()
           //   user.getPassword(),
           //   user.getChats()
      );

  }

}
