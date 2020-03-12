package com.myservice.example.controllers;

import com.myservice.example.exceptions.UserException;
import com.myservice.example.testing.*;
import com.myservice.example.users.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TestingController {

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private TestingSessionStorage testingSessionStorage;

    @GetMapping("/questions")   //Запуск теста, получение списка вопросов
    public Set<Question> getQuestions (@RequestParam(name = "token") String token) throws SQLException {
        userDataRepository.checkAuthorization(token);

        Test test = new Test();
        testingSessionStorage.getUsersTest().put(token,test);
        return test.getQuestionsWithAnswers().keySet();
    }

    @GetMapping("/answers")     //Получение списка ответов к вопросам
    public Set<List<Answer>> getAnswers (@RequestParam(name = "token") String token) {
        userDataRepository.checkAuthorization(token);

        Test test = testingSessionStorage.getUsersTest().get(token);
        return test.getQuestionsWithAnswers().entrySet().stream()
                .filter(e -> e.getKey().getQ_type() == 2)
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    @PostMapping("/check")      //Проверка результатов теста их сохранение
    public String getResult (@RequestParam(name = "token") String token,
                           @RequestParam Map<String,String> answers) {
        userDataRepository.checkAuthorization(token);

        Set<Question> testData = testingSessionStorage.getUsersTest().get(token).getQuestionsWithAnswers().keySet();

        int counter = 0;
        for (Question q : testData) {
            if (q.getRight_answer().equalsIgnoreCase(answers.get(String.valueOf(q.getId())))) {
                counter++;
            }
        }
        testingSessionStorage.getUsersTest().remove(token);

        String username = userDataRepository.getUserNameByToken(token);
        userDataRepository.saveUsersResultsToDataBase(username,counter,testData.size());

        String result = (counter == testData.size()) ? "Testing successfully done.\n" : "Testing failed.\n";
        return String.format(result + "Right answers: %d of %d.", counter, testData.size());
    }
}
