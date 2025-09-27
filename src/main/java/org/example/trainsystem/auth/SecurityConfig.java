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
                        // 🔹 only Admins can manage users/routes
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 🔹 Operation Managers dashboard & features
                        .requestMatchers("/opmanager/**").hasRole("OPMANAGER")

                        // 🔹 Drivers dashboard & routes
                        .requestMatchers("/driver/**").hasRole("DRIVER")

                        // 🔹 passenger
                        .requestMatchers("/passenger/**").hasRole("PASSENGER")

                        // 🔹 Public pages like login/register
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()

                        // everything else requires login
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // ❗ disable CSRF for now (you can enable later)

                .formLogin(form -> form
                        .loginPage("/login")                 // your login.html
                        .loginProcessingUrl("/login")        // POST action in your form
                        .successHandler(successHandler)      // custom success redirect
                        .failureUrl("/login?error=true")     // show error on failed login
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
