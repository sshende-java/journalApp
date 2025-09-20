package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    private UserService userService;

    private AuthenticationManager authenticationManager;

    private UserDetailsServiceImpl userDetailsService;

    private JwtUtil jwtUtil;

    @Autowired
    public PublicController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> createEntry(@RequestBody User user) {
        user.setRoles(new ArrayList<>(Arrays.asList("User")));      //Spring security needs one Dummy Role, if u dont want create your own class which implements UserDetails.java interface
        boolean isSaved = userService.saveEntry(user);
        if (isSaved)
            return new ResponseEntity<>(HttpStatus.CREATED);
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            //check if user and password is correct in db, if incorrect exception is thrown, behind the scenes it calls - UserDetailsService.loadUserByUsername(...)
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword())
            );
            log.info(auth.getName());
            log.info(user.getUserName());
            UserDetails userDetails = (UserDetails) auth.getPrincipal();        //auth.getPrincipal() contains the UserDetails loaded during authentication.

            log.info("******* {}",userDetails.getAuthorities().toString());

            String jwt = jwtUtil.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
            return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception Occured while creating JWT token for user ", e);
            return new ResponseEntity<>("Incorrect Username or password !!", HttpStatus.BAD_REQUEST);
        }

    }
}
