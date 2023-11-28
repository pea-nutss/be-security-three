package com.aaronbujatin.securitythree.service;

import com.aaronbujatin.securitythree.dto.RegisterDto;
import com.aaronbujatin.securitythree.exception.UserAlreadyExistsException;
import com.aaronbujatin.securitythree.model.IUser;
import com.aaronbujatin.securitythree.model.Role;
import com.aaronbujatin.securitythree.repository.RoleRepository;
import com.aaronbujatin.securitythree.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public IUser register(RegisterDto registerDto){
        Boolean isExists = userRepository.existsByUsername(registerDto.getUsername());
        if(!isExists){
            IUser user = new IUser();
            user.setUsername(registerDto.getUsername());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            Role role = roleRepository.findByName("ROLE_USER");
            //setting the role of registered user to ROLE_USER, By the use of singleton
            user.setRole(Collections.singleton(role));



            return userRepository.save(user);
        }else{
            throw new UserAlreadyExistsException("Username already exists. Try another");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username : " + username + " was not found"));


        return new User(user.getUsername(), user.getPassword(), getRoles(user.getRole()));
    }

    private List<GrantedAuthority> getRoles(Set<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
