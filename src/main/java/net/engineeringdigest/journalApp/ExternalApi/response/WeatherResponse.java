package net.engineeringdigest.journalApp.ExternalApi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data  //getter setter RequiredArgsConstructor
public class WeatherResponse {


    private Current current;

    @Data  //getter setter RequiredArgsConstructor
    public class Current {

        public int temperature;

        @JsonProperty("weather_descriptions")
        public ArrayList<String> weather_descriptions;

        public int feelslike;

    }


}
