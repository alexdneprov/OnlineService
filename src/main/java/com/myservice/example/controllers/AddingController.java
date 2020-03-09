package com.myservice.example.controllers;

import com.myservice.example.exceptions.UserNotFound;
import com.myservice.example.testing.QuestionAdder;
import com.myservice.example.users.UserDataRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping
public class AddingController {

    @PostMapping("/addQuestion")
    public String addFree (@RequestParam (name = "token") String token,
                           @RequestParam Map<String,String> map) throws SQLException, UserNotFound {
        map.remove("token");
        if (!UserDataRepository.getAuthorizedUsers().containsValue(token)) throw new UserNotFound();
        return QuestionAdder.addQuestion(map);
    }
}
