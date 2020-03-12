package com.myservice.example.testing;

import com.myservice.example.exceptions.AddingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QuestionAdder {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public synchronized HttpStatus addQuestion (Map<String,String> map) throws AddingException {

        if (map.size() == 2) {
            String addQuestion = "INSERT INTO onlineservice.questions (q_type, question, right_answer) VALUES (?, ?, ?)";
            jdbcTemplate.update(addQuestion,1, map.get("question"), map.get("rightAnswer"));

            String getQuestion = "SELECT id FROM questions ORDER BY id DESC LIMIT 1;";
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(getQuestion);
            rowSet.next();
            int questionId = rowSet.getInt("id");

            String addAnswer = "INSERT INTO onlineservice.answers (question_id,right_answer) VALUES (?, ?)";
            jdbcTemplate.update(addAnswer, questionId, map.get("rightAnswer"));

            return HttpStatus.CREATED;

        }else if (map.size() == 5) {
            String addQuestion = "INSERT INTO onlineservice.questions (q_type, question, right_answer) VALUES (?, ?, ?)";
            jdbcTemplate.update(addQuestion,2, map.get("question"), map.get("rightAnswer"));

            String getQuestion = "SELECT id FROM questions ORDER BY id DESC LIMIT 1;";
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(getQuestion);
            rowSet.next();
            int questionId = rowSet.getInt("id");

            String addAnswer1 = "INSERT INTO onlineservice.answers (answer, question_id,right_answer) VALUES (?, ?, ?)";
            String addAnswer2 = "INSERT INTO onlineservice.answers (answer, question_id,right_answer) VALUES (?, ?, ?)";
            String addAnswer3 = "INSERT INTO onlineservice.answers (answer, question_id,right_answer) VALUES (?, ?, ?)";
            String addAnswerRight = "INSERT INTO onlineservice.answers (answer, question_id,right_answer) VALUES (?, ?, ?)";
            jdbcTemplate.update(addAnswer1,map.get("option1"),questionId,map.get("rightAnswer"));
            jdbcTemplate.update(addAnswer2,map.get("option2"),questionId,map.get("rightAnswer"));
            jdbcTemplate.update(addAnswer3,map.get("option3"),questionId,map.get("rightAnswer"));
            jdbcTemplate.update(addAnswerRight,map.get("rightAnswer"),questionId,map.get("rightAnswer"));

            return HttpStatus.CREATED;
        }
        throw new AddingException(HttpStatus.BAD_REQUEST,"Invalid input parameters");
    }
}
