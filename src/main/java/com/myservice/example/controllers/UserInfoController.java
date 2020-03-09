package com.myservice.example.controllers;

import com.myservice.example.users.UserInfo;
import com.myservice.example.users.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("users")
public class UserInfoController {

    @Autowired
    private UserDataRepository userDataRepository;

    @GetMapping("/users")
    public Set<String> getRegisteredUsers() {
        return userDataRepository.getAllRegisteredUsers();
    }

    @GetMapping("/{username}")
    public UserInfo getUserInfo(@PathVariable String username) {
        return userDataRepository.getUserInfo(username);
    }
}
