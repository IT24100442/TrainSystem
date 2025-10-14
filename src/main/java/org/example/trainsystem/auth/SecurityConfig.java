package org.example.trainsystem.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserLoginSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 🔹 Public pages first
                        .requestMatchers("/",  "/register-passenger", "/registration", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()

                        // 🔹 only Admins can manage users/routes
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 🔹 Operation Managers dashboard & features
                        .requestMatchers("/opmanager/**").hasRole("OPMANAGER")

                        // 🔹 Drivers dashboard & routes
                        .requestMatchers("/driver/**").hasRole("DRIVER")

                        // ticket officer dashboard
                        .requestMatchers("/ticket/**").hasRole("TICKETOFFICER")

                        //🔹 IT Officer dashboard & routes
                         .requestMatchers("/it/**").hasRole("ITOFFICER")

                        // 🔹 Passenger pages
                        .requestMatchers("/passenger/**").hasRole("PASSENGER")


                        // everything else requires login
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())

                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .successHandler(successHandler)
                        .permitAll()
                )


                .logout(logout -> logout
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(COOKIES)))
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
