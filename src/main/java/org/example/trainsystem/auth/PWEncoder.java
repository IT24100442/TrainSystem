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

        System.out.println(new BCryptPasswordEncoder().encode("12345"));
        // pwd: $2a$10$VKNplZV7wQChmOJz3GjJsuY.G0sVndN6Um91PSPpKkR0Mzx2mnk6G

        System.out.println(new BCryptPasswordEncoder().encode("23456"));
        // pwd: $2a$10$QbdE81imZyOYd/nL8DJj3u1WZxxp1pxIX/cU5N2X8aEoQzFe4j2iK


    }

}