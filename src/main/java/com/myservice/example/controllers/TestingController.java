package com.myservice.example.controllers;

import com.myservice.example.testing.*;
import com.myservice.example.users.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TestingController {

    @Autowired
    private UserDataRepository userDataRepository;

    @GetMapping("/questions")   //Запуск теста, получение списка вопросов
    public Set<Question> getQuestions (@RequestParam(name = "token") String token) throws SQLException {
        Test test = new Test();
        TestingSessionStorage.getUsersTest().put(token,test);
        return test.getQuestionsWithAnswers().keySet();
    }

    @GetMapping("/answers")     //Получение списка ответов к вопросам
    public Set<List<Answer>> getAnswers (@RequestParam(name = "token") String token) {
        Test test = TestingSessionStorage.getUsersTest().get(token);

        return test.getQuestionsWithAnswers().entrySet().stream()
                .filter(e -> e.getKey().getQ_type() == 2)
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    @PostMapping("/check")      //Проверка результатов теста их сохранение
    public String getResult (@RequestParam(name = "token") String token,
                           @RequestParam Map<String,String> answers) {
        Set<Question> testData = TestingSessionStorage.getUsersTest().get(token).getQuestionsWithAnswers().keySet();

        int counter = 0;
        for (Question q : testData) {
            if (q.getRight_answer().equalsIgnoreCase(answers.get(String.valueOf(q.getId())))) {
                counter++;
            }
        }
        TestingSessionStorage.getUsersTest().remove(token);    //удаление перечня вопросов и ответов

        String username = userDataRepository.getUserNameByToken(token);
        userDataRepository.saveUsersResultsToDataBase(username,counter,testData.size());

        String result = (counter == testData.size()) ? "Testing successfully done.\n" : "Testing finished.\n";
        return String.format(result + "Right answers: %d of %d.", counter, testData.size());
    }
}
