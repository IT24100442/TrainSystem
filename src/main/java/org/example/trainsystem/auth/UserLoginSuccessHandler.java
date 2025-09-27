package org.example.trainsystem.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        String contextPath = request.getContextPath(); // e.g. /trainsystem
        String redirectURL = contextPath + "/"; // default

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                redirectURL = contextPath + "/admin/dashboard";
                break;
            } else if (role.equals("ROLE_PASSENGER")) {
                redirectURL = contextPath + "/passenger/dashboard";
                break;
            } else if (role.equals("ROLE_DRIVER")) {
                redirectURL = contextPath + "/driver/dashboard";
                break;
            } else if (role.equals("ROLE_OPMANAGER")) {
                redirectURL = contextPath + "/opmanager/dashboard";
                break;
            }
        }

        response.sendRedirect(redirectURL);
    }
}
