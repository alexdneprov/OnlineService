package com.myservice.example.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DATABASE_URL = "jdbc:mysql://localhost/onlineservice?useUnicode=true&serverTimezone=UTC";

    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    static {
        try {
            connection = DriverManager.getConnection(DATABASE_URL,"root","qwerty123+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
