package com.myservice.example.users;

import com.myservice.example.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class UserDataRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //Authorized Users: map key - username; map value - token.
    private Map<String,String> authorizedUsers = new HashMap<>();

    public Set<String> getAuthorizedUsers() {
        return authorizedUsers.keySet();
    }

    public String getUserNameByToken (String token) {
        return this.authorizedUsers.entrySet().stream()
                .filter(e -> e.getValue().equals(token))
                .map(Map.Entry::getKey)
                .findFirst()
                .get();
    }

    public boolean checkAuthorization(String token) {
        if (authorizedUsers.containsValue(token)) return true;
        throw new UserException(HttpStatus.UNAUTHORIZED, "User not authorized or token incorrect");
    }

    public String login (String userName, String password) throws SQLException, UserException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String queryGetUserByName = "SELECT username, password FROM users WHERE username=?";

        String u = null;        String p = null;
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryGetUserByName,userName);
        while (rowSet.next()) {
            u = rowSet.getString("username");
            p = rowSet.getString("password");
        }

        if (u == null) {
            throw new UserException(HttpStatus.NOT_FOUND, "User does not exist");
        }else if (encoder.matches(password,p)) {
            if (authorizedUsers.containsKey(userName)) throw new UserException(HttpStatus.BAD_REQUEST, "User has already authorized");
            String token = TokenGenerator.getToken();
            authorizedUsers.put(userName,token);
            return token;
        }else{
            throw new UserException(HttpStatus.NOT_ACCEPTABLE, "Password incorrect");
        }
    }

    public HttpStatus logout (String token) {
        if (checkAuthorization(token)) {
            for (Map.Entry<String, String> s : this.authorizedUsers.entrySet()){
                if (s.getValue().equals(token)) getAuthorizedUsers().remove(s.getKey());
            }
        }
        return HttpStatus.OK;
    }

    public HttpStatus addNewUser(String userName, String password) throws SQLException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String encodedPass = encoder.encode(password);

        String checkUserQuery = "SELECT COUNT(*) FROM users WHERE username=?";

        int exists = jdbcTemplate.queryForObject(checkUserQuery,new Object[]{userName},Integer.class);
        if (exists > 0) throw new UserException(HttpStatus.NOT_ACCEPTABLE, "User " + userName + " already exists.");

        jdbcTemplate.update("INSERT INTO users values (?,?,?,?)",userName,encodedPass,0,0);
        return HttpStatus.CREATED;
    }

    public Set<String> getAllRegisteredUsers() {
        Set<String> usersSet = new HashSet<>();

        String getAllUsers = "SELECT * FROM users";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(getAllUsers);

        while (rowSet.next()) {
            usersSet.add(rowSet.getString("username"));
        }
        return usersSet;
    }

    public UserInfo getUserInfo (String username) throws UserException {
        String sql = "SELECT username,done_testing,done_right_testing FROM users WHERE username = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql,username);

        String name = null; boolean done_testing = false; boolean done_right_testing = false;
        while (rows.next()) {
            name = rows.getString("username");
            done_testing = rows.getInt("done_testing") == 1;
            done_right_testing = rows.getInt("done_right_testing") == 1;
        }

        if (name == null) throw new UserException(HttpStatus.NOT_FOUND, "User does not exists");
        return new UserInfo(name,done_testing,done_right_testing);
    }

    public HttpStatus saveUsersResultsToDataBase(String username, int rightAnswersCounter, int questionsNumber) {
        if (rightAnswersCounter == questionsNumber) {
            String sqlRight = "UPDATE onlineservice.users SET done_testing = ?, done_right_testing = ? WHERE (username = ?)";
            jdbcTemplate.update(sqlRight,1,1,username);
        }else {
            String sqlDone = "UPDATE onlineservice.users SET done_testing = ?, done_right_testing = ? WHERE (username = ?)";
            jdbcTemplate.update(sqlDone,1,0,username);
        }
        return HttpStatus.ACCEPTED;
    }
}
