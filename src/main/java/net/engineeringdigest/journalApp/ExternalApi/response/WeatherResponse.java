package net.engineeringdigest.journalApp.ExternalApi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;

@Data  //getter setter RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {


    private Current current;

    @Data  //getter setter RequiredArgsConstructor
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Current {       //made this class as static so that Jackson can access it without instance

        public int temperature;

        @JsonProperty("weather_descriptions")
        public ArrayList<String> weather_descriptions;

        public int feelslike;

    }


}
