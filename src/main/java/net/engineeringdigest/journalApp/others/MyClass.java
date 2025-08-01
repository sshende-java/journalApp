package net.engineeringdigest.journalApp.others;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyClass {

    @Autowired
    private Dog dog;

    @GetMapping("/abc")
    public String sayHello(){
        System.out.println(dog.getName());
        System.out.println(dog.getAge());
        return  "hello";
    }
}
