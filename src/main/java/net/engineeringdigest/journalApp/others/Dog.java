package net.engineeringdigest.journalApp.others;

import org.springframework.stereotype.Component;

@Component
public class Dog {
    private String name;
    private int age;

    public String getName(){
        return  name;
    }
    public int getAge(){
        return age;
    }
}
