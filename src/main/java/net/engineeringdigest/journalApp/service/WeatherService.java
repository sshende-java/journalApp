package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.ExternalApi.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;
    private static final String API = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;


    public WeatherResponse getWeather(String city) {
        String finalAPIUrl = API.replace("CITY", city).replace("API_KEY", apiKey);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPIUrl, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse weatherResponse = response.getBody();
        return weatherResponse;
    }

}
