package com.myservice.example.controllers;

import com.myservice.example.exceptions.AddingException;
import com.myservice.example.exceptions.UserException;
import com.myservice.example.testing.QuestionAdder;
import com.myservice.example.users.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AddingController {

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private QuestionAdder questionAdder;

    @PostMapping("/addQuestion")
    public HttpStatus addFree (@RequestParam (name = "token") String token,
                               @RequestParam Map<String,String> map) throws UserException, AddingException {
        map.remove("token");
        userDataRepository.checkAuthorization(token);
        return questionAdder.addQuestion(map);
    }
}
