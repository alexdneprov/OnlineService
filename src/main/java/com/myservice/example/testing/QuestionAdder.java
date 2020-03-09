package com.myservice.example.testing;

import com.myservice.example.storage.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public abstract class QuestionAdder {

    public static synchronized String addQuestion (Map<String,String> map) throws SQLException {

        Statement statement = DatabaseConnection.getConnection().createStatement();
        System.out.println(map);

        if (map.size() == 2) {
            String addQuestion = "INSERT INTO `onlineservice`.`questions` (`q_type`, `question`, `right_answer`) VALUES ('1', '" + map.get("question") + "', '" + map.get("rightAnswer") + "');\n";
            statement.executeUpdate(addQuestion);

            String getQuestion = "SELECT id FROM questions ORDER BY id DESC LIMIT 1;";
            ResultSet resultSet = statement.executeQuery(getQuestion);

            resultSet.next();
            int questionId = resultSet.getInt("id");

            String addAnswer = "INSERT INTO onlineservice.answers (question_id,right_answer) VALUES ('" + questionId + "', '" + map.get("rightAnswer") + "')";
            statement.executeUpdate(addAnswer);
            statement.close();

            return "Your question has successfully added";

        }else if (map.size() == 5) {
            String addQuestion = "INSERT INTO `onlineservice`.`questions` (`q_type`, `question`, `right_answer`) VALUES ('2', '" + map.get("question") + "', '" + map.get("rightAnswer") + "');\n";
            statement.executeUpdate(addQuestion);

            String getQuestion = "SELECT id FROM questions ORDER BY id DESC LIMIT 1;";
            ResultSet resultSet = statement.executeQuery(getQuestion);

            resultSet.next();
            int questionId = resultSet.getInt("id");

            String addAnswer1 = "INSERT INTO onlineservice.answers (answer, question_id,right_answer) VALUES ('" + map.get("option1") + "' ,'" + questionId + "', '" + map.get("rightAnswer") + "')";
            String addAnswer2 = "INSERT INTO onlineservice.answers (answer, question_id,right_answer) VALUES ('" + map.get("option2") + "' ,'" + questionId + "', '" + map.get("rightAnswer") + "')";
            String addAnswer3 = "INSERT INTO onlineservice.answers (answer, question_id,right_answer) VALUES ('" + map.get("option3") + "' ,'" + questionId + "', '" + map.get("rightAnswer") + "')";
            String addAnswerRight = "INSERT INTO onlineservice.answers (answer, question_id,right_answer) VALUES ('" + map.get("rightAnswer") + "' ,'" + questionId + "', '" + map.get("rightAnswer") + "')";
            statement.executeUpdate(addAnswer1);
            statement.executeUpdate(addAnswer2);
            statement.executeUpdate(addAnswer3);
            statement.executeUpdate(addAnswerRight);
            statement.close();

            return "Your question has successfully added";
        }
        return "Error";
    }
}
