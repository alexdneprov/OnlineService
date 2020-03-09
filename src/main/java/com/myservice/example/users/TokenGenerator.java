package com.myservice.example.users;

import java.security.SecureRandom;

public abstract class TokenGenerator {

    public static String getToken() {
        SecureRandom random = new SecureRandom();
        int limit = 15;

        String generatedToken = random.ints(48,122)
                .filter(c -> (c >= 48 && c <=57) || (c >=65 && c <= 90) || (c >= 97 && c <= 122))
                .limit(limit)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        System.out.println(generatedToken);
        return generatedToken;
    }
}
