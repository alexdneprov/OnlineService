package com.myservice.example.users;

import com.myservice.example.storage.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class UserDataRepository {

    // Singleton Instance of UserDataRepository
    private static UserDataRepository instance;

    private UserDataRepository () {
    }

    public static synchronized UserDataRepository getInstance() {
        if (instance == null) {
            instance = new UserDataRepository();
        }
        return instance;
    }

    //Authorized Users: map key - username; map value - token.
    private static Map<String,String> authorizedUsers = new HashMap<>();

    public static Map<String, String> getAuthorizedUsers() {
        return authorizedUsers;
    }
    public static String getUserNameByToken (String token) {
        return getAuthorizedUsers().entrySet().stream()
                .filter(e -> e.getValue().equals(token))
                .map(Map.Entry::getKey)
                .findFirst()
                .get();
    }

    @Autowired
    private DataSource dataSource;

    public String login (String userName, String password) throws SQLException {
        //String queryGetUserByName = "SELECT username, password, FROM users WHERE username='"+userName+"';";
        String queryGetUserByName = "SELECT username FROM users WHERE username=?";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String s = jdbcTemplate.queryForObject(queryGetUserByName, new Object[]{userName},String.class);
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryGetUserByName);
        String u = rowSet.getString("username");
        String p = rowSet.getString("password");

        /*Statement statement = DatabaseConnection.getConnection().createStatement();
        ResultSet result = statement.executeQuery(queryGetUserByName);

        String u = null;        String p = null;
        while (result.next()) {
            u = result.getString("username");
            p = result.getString("password");
        }*/

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (u == null) {
            return "User does not exists";
        }else if (encoder.matches(password,p)) {
            //statement.close();

            if (authorizedUsers.containsKey(userName)) return "User already been authorized";
            String token = TokenGenerator.getToken();
            authorizedUsers.put(userName,token);
            return token;
        }else{
            //statement.close();
            return "Password incorrect";
        }
    }

    public static String logout (String token) {
        if (getAuthorizedUsers().containsValue(token)) {
            for (Map.Entry<String, String> s : getAuthorizedUsers().entrySet()){
                if (s.getValue().equals(token)) getAuthorizedUsers().remove(s.getKey());
            }
            return "User logged out";
        }
        return "User not authorized or token incorrect";
    }

    public static String addNewUser(String userName, String password) throws SQLException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String encodedPass = encoder.encode(password);

        Statement statement = DatabaseConnection.getConnection().createStatement();
        String checkUserQuery = "SELECT username FROM users WHERE username='"+userName+"';";
        ResultSet result = statement.executeQuery(checkUserQuery);

        if(result.next()){
            String userNameFromDb = result.getString("username");
            statement.close();
            return "User" + userNameFromDb + "already exists.";
        }

        String addUserQuery = "INSERT INTO users values ('"+userName+"','"+encodedPass+"', 0, 0)";
        statement.executeUpdate(addUserQuery);
        statement.close();
        return "User " + userName + " registered. Now you have to log in.";
    }

    public static Set<String> getAllRegisteredUsers() {
        Set<String> usersSet = new HashSet<>();

        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String getAllUsers = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(getAllUsers);
            while(resultSet.next()) {
                usersSet.add(resultSet.getString("username"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return usersSet;
    }

    public static UserInfo getUserInfo (String username) {
        UserInfo userInfo = null;

        Statement statement;
        try {
            statement = DatabaseConnection.getConnection().createStatement();
            String checkUserQuery = "SELECT username,done_testing,done_right_testing FROM users WHERE username ='"+username+"';";
            ResultSet result = statement.executeQuery(checkUserQuery);
            result.next();

            String userName = result.getString("username");
            String done_testing = String.valueOf(result.getInt("done_testing"));
            String done_testing_right = String.valueOf(result.getInt("done_right_testing"));

            userInfo = new UserInfo(userName,Boolean.parseBoolean(done_testing),Boolean.parseBoolean(done_testing_right));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public static void saveUsersResultsToDataBase(String username, int rightAnswersCounter, int questionsNumber) {
        Statement statement;
        try {
            statement = DatabaseConnection.getConnection().createStatement();

            if (rightAnswersCounter == questionsNumber) {
                String doneRightQuery = "UPDATE `onlineservice`.`users` SET `done_testing` = '1', `done_right_testing` = '1' WHERE (`username` = '" + username + "');";
                statement.executeUpdate(doneRightQuery);
                statement.close();
            }else {
                String doneQuery = "UPDATE `onlineservice`.`users` SET `done_testing` = '1', `done_right_testing` = '0' WHERE (`username` = '" + username + "');";
                statement.executeUpdate(doneQuery);
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
