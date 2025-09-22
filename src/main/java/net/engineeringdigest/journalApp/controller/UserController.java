package net.engineeringdigest.journalApp.controller;


import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


/*
    @GetMapping                   you cant see all users now only admin can
    public List<User> getAllUsers() {
        return userService.getAll();
    }
*/

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUserName(userName);

        userInDb.setUserName(updatedUser.getUserName());
        userInDb.setPassword(updatedUser.getPassword());
        userService.saveEntry(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        userService.deleteUserByName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Global rate limiting (same for everyone hitting the endpoint)
    // ratelimiterInstance1 (defined in .yml) current rate limiting is not user-specific or IP-specific. its for all ie global
    @RateLimiter(name="ratelimiterInstance1",fallbackMethod = "getMessageRateLimitFallback")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> all = userService.getAll();
        return ResponseEntity.ok(all);
    }

    public ResponseEntity<?> getMessageRateLimitFallback(Throwable e) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Rate limit exceeded. Please try again later.");
    }
}
