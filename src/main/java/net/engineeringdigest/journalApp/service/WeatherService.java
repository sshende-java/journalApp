package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.ExternalApi.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city) {
        //fetch from redis cache using service
        WeatherResponse cachedWeather = redisService.get(city, WeatherResponse.class);

        if (cachedWeather != null) {
            return cachedWeather;
        } else {
            String finalAPIUrl = API.replace("CITY", city).replace("API_KEY", apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPIUrl, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse fetchedWeatherResponse = response.getBody();
            if (fetchedWeatherResponse != null) {
                //add to redis cache using redisService
                redisService.set(city, fetchedWeatherResponse, 4L);
            }

            return fetchedWeatherResponse;
        }

    }

    //this method is specifically written to demonstrate the use of @Cacheable annotation for redis cache   redis-cli : get weather::Delhi
    @Cacheable(value = "weather",key = "#city")
    public WeatherResponse getWeather2(String city) {
        String finalAPIUrl = API.replace("CITY", city).replace("API_KEY", apiKey);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPIUrl, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse fetchedWeatherResponse = response.getBody();
        System.out.println("in getWeather2 :");
        return fetchedWeatherResponse;
    }


}
