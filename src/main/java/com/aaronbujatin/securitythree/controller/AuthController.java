package com.aaronbujatin.securitythree.controller;

import com.aaronbujatin.securitythree.dto.AuthResponseDto;
import com.aaronbujatin.securitythree.dto.LoginDto;
import com.aaronbujatin.securitythree.dto.RegisterDto;
import com.aaronbujatin.securitythree.security.JwtGenerator;
import com.aaronbujatin.securitythree.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        userDetailsServiceImpl.register(registerDto);
        return new ResponseEntity<>("User successfully registered", HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                //this will get the incoming username(principal) and password(credentials) and store in  'authentication' variable
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        //we set the SecurityContextHolder from the value of authentication variable.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //then we generate the token
        String token = jwtGenerator.generateToken(authentication);

    return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }

}
