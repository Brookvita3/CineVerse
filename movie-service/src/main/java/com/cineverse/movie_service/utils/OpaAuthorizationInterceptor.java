package com.cineverse.movie_service.utils;

import com.cineverse.movie_service.exception.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OpaAuthorizationInterceptor implements HandlerInterceptor {

    private final OPAUtils opaUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String[] pathSegments = uri.substring(1).split("/");
        String role = request.getHeader("X-Role");

        boolean authorized = opaUtils.checkAuthorization(method, pathSegments, role);

        if (!authorized) {
            throw new AccessDeniedException("Not authorized by OPA");
        }

        return true;
    }
}

