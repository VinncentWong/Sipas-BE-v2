package org.example.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptUtil {

    private static final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    public static String encode(String rawPassword){
        return bcrypt.encode(rawPassword);
    }

    public static boolean isMatch(String rawPassword, String encodedPassword){
        return bcrypt.matches(rawPassword, encodedPassword);
    }
}
