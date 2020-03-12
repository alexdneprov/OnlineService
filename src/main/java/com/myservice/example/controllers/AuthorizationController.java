package com.myservice.example.controllers;

import com.myservice.example.exceptions.UserException;
import com.myservice.example.users.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Set;

@RestController
public class AuthorizationController {

    @Autowired
    private UserDataRepository userDataRepository;

    @GetMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) throws SQLException, UserException {
        return userDataRepository.login(username, password);
    }

    @GetMapping("/logout")
    public HttpStatus logout (@RequestParam String token) {
        return userDataRepository.logout(token);
    }

    @PostMapping("/registerNewUser")
    public HttpStatus registration (@RequestParam String username,
                                    @RequestParam String password) throws SQLException {
        return userDataRepository.addNewUser(username,password);
    }

    @GetMapping("/authorizedUsers")
    public Set<String> getAuthorizedUsers () {
        return userDataRepository.getAuthorizedUsers();
    }
}
