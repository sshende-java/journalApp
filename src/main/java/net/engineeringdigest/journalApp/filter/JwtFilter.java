package net.engineeringdigest.journalApp.filter;

import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import net.engineeringdigest.journalApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);     //after "Bearer " - 7 length
            System.out.println("Received token: [" + jwt + "]"); // Print brackets to see spaces
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null) {
            // 👇 Skip DB call because not feasible to make DB call for every request - using token only
            // UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // ✅ Validate token signature and expiration
            if (jwtUtil.validateToken(jwt)){
                // ✅ Extract roles from token
                List<GrantedAuthority> authorities = jwtUtil.extractRoles(jwt)
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());


                // ✅ Build UserDetails manually (no DB)
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        username,
                        "", // password is not needed here
                        authorities
                );

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        response.addHeader("admin", "saurabh");

        System.out.println("Before filterChain.doFilter");
        filterChain.doFilter(request, response);
        System.out.println("After filterChain.doFilter");
    }
}
