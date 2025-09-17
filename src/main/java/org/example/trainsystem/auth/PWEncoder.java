package org.example.trainsystem.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PWEncoder {

    public static String encode(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
//Password:  $2a$10$fZEBlItGg93.9QTY2Dqs8Okt2pJ7rUX.//eq7J1xDFEoYJDHz.P1q
    public static void main(String[] args) {
        String rawPassword = "123";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
    }

}