package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.ExternalApi.response.PostResponse;
import net.engineeringdigest.journalApp.ExternalApi.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalPostService {


    private static final String API = "https://jsonplaceholder.typicode.com/posts";

    @Autowired
    private RestTemplate restTemplate;


    public PostResponse savePost(PostResponse post) {
        HttpEntity<PostResponse> httpEntity = new HttpEntity<>(post);
        ResponseEntity<PostResponse> response = restTemplate.exchange(API, HttpMethod.POST, httpEntity, PostResponse.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            PostResponse finalResponse = response.getBody();
            return finalResponse;
        }

        throw new RuntimeException("Cannot create post");
    }

}
