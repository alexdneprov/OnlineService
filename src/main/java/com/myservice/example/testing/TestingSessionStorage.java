package com.myservice.example.testing;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TestingSessionStorage {

    private Map<String,Test> usersTest = new HashMap<>();

    public Map<String, Test> getUsersTest() {
        return usersTest;
    }
}
