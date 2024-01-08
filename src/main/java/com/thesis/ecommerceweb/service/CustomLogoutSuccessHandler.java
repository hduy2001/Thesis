package com.thesis.ecommerceweb.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String referer = request.getHeader("Referer");

        // Set the default URL to redirect after logout
        String targetUrl = "/";

        // If the referer is available, set it as the target URL
        if (referer != null && !referer.contains("/logout")) {
            targetUrl = referer;
        }

        // Redirect to the target URL after logout
        response.sendRedirect(request.getContextPath() + targetUrl);
    }
}
