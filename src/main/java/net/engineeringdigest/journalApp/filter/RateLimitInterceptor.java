package net.engineeringdigest.journalApp.filter;

import net.engineeringdigest.journalApp.service.UserRateLimiterService;
import net.engineeringdigest.journalApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {


    @Autowired
    private UserRateLimiterService rateLimiterService;

    @Autowired
    private JwtUtil jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {

//        String authHeader = request.getHeader("Authorization");
//        String userId = jwtUtils.extractUsername(authHeader);

        String authorizationHeader = request.getHeader("Authorization");
        String userId = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);         //after "Bearer " - 7 length
            System.out.println("Received token: [" + jwt + "]");      // Print brackets to see spaces
            userId = jwtUtils.extractUsername(jwt);
        }


        if (!rateLimiterService.tryConsume(userId)) {
            response.setStatus(429);
            response.getWriter().write("Rate limit exceeded for user: " + userId);
            return false;
        }

        return true;
    }
}
