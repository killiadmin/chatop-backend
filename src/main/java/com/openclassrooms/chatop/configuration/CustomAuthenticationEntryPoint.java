package com.openclassrooms.chatop.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Responds to unauthorized requests by setting the HTTP status code to 401 (Unauthorized)
     * and setting the content length to zero.
     *
     * @param request       the HTTP request that resulted in an authentication failure
     * @param response      the HTTP response to be sent to the requesting client
     * @param authException the exception that caused the authentication failure
     * @throws IOException if an input or output exception occurs while writing the response
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentLength(0);
    }
}
