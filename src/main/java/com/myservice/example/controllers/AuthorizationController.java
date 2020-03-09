package com.myservice.example.controllers;

import com.myservice.example.users.TokenGenerator;
import com.myservice.example.users.UserDataRepository;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping
public class AuthorizationController {

    @GetMapping("/login")
    public String login(@RequestParam String username, String password) throws SQLException {
        UserDataRepository userDataRepository = UserDataRepository.getInstance();
        return userDataRepository.login(username, password);
    }

    @GetMapping("/logout")
    public String logout (@RequestParam String token) {
        return UserDataRepository.logout(token);
    }

    @PostMapping("/registerNewUser")
    public String registration (@RequestParam String username, String password) throws SQLException {
        return UserDataRepository.addNewUser(username,password);
    }

    @GetMapping("/authorizedUsers")
    public Map<String, String> getAuthorizedUsers () {
        return UserDataRepository.getAuthorizedUsers();
    }
}
