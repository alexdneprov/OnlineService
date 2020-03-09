package com.myservice.example.controllers;

import com.myservice.example.users.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
public class AuthorizationController {

    @Autowired
    private UserDataRepository userDataRepository;

    @GetMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) throws SQLException {
        return userDataRepository.login(username, password);
    }

    @GetMapping("/logout")
    public String logout (@RequestParam String token) {
        return userDataRepository.logout(token);
    }

    @PostMapping("/registerNewUser")
    public String registration (@RequestParam String username,
                                @RequestParam String password) throws SQLException {
        return userDataRepository.addNewUser(username,password);
    }

    @GetMapping("/authorizedUsers")
    public Map<String, String> getAuthorizedUsers () {
        return userDataRepository.getAuthorizedUsers();
    }
}
