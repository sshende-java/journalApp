package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.ExternalApi.response.PostResponse;
import net.engineeringdigest.journalApp.ExternalApi.response.WeatherResponse;
import net.engineeringdigest.journalApp.service.ExternalPostService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/external")
public class ExternalAPIController {


    @Autowired
    private WeatherService weatherService;

    @Autowired
    private ExternalPostService externalPostService;


    //some - external api for get
    @GetMapping
    public ResponseEntity<?> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResp = weatherService.getWeather("Mumbai");
        String greeting = "";
        if (weatherResp != null) {
            greeting = " Weather feels like " + weatherResp.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + greeting, HttpStatus.OK);
    }

    //some external-api for post
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostResponse post) {

        try {
            PostResponse postResponse = externalPostService.savePost(post);
            return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
