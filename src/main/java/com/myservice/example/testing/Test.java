package com.myservice.example.testing;

import com.myservice.example.storage.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Test {

    private Map<Question, List<Answer>> questionsWithAnswers = new Hashtable<>();

    public Test() throws SQLException {
        initializeQuestionsWithAnswers();
    }

    private void initializeQuestionsWithAnswers () throws SQLException {
        Statement statementQ = DatabaseConnection.getConnection().createStatement();
        String queryQuestions = "SELECT * FROM questions";

        ResultSet resultSetQ = statementQ.executeQuery(queryQuestions);

        while (resultSetQ.next()) {
            int idQ = resultSetQ.getInt("id");
            int q_type = resultSetQ.getInt("q_type");
            String q = resultSetQ.getString("question");
            String rA = resultSetQ.getString("right_answer");

            Question questionObject = new Question(idQ,q_type,q,rA);

            Statement statementA = DatabaseConnection.getConnection().createStatement();
            String queryAnswers = "SELECT * FROM answers WHERE question_id="+idQ;
            ResultSet resultSetA = statementA.executeQuery(queryAnswers);

            List<Answer> answerList = new ArrayList<>();
            while (resultSetA.next()) {
                int idA = resultSetA.getInt("id");
                String answer = resultSetA.getString("answer");
                int question_id = resultSetA.getInt("question_id");
                String right_answer = resultSetA.getString("right_answer");

                Answer answerObject = new Answer(idA,answer,question_id,right_answer);
                answerList.add(answerObject);
            }
            questionsWithAnswers.put(questionObject,answerList);
        }
    }

    public Map<Question, List<Answer>> getQuestionsWithAnswers() {
        return questionsWithAnswers;
    }
}
