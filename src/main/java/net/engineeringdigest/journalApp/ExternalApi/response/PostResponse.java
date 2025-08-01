package net.engineeringdigest.journalApp.ExternalApi.response;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class PostResponse {
    public int userId;
    public int id;
    public String title;
    public String body;
}
