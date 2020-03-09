package com.myservice.example.testing;

import java.util.HashMap;
import java.util.Map;

public abstract class TestingSessionStorage {

    private static Map<String,Test> usersTest = new HashMap<>();

    public static Map<String, Test> getUsersTest() {
        return usersTest;
    }
}
