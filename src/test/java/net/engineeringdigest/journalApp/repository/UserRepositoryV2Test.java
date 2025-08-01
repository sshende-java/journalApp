package net.engineeringdigest.journalApp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryV2Test {

    @Autowired
    private UserRepositoryV2 userRepositoryV2;

    @Test
    public void getUserForSATest(){
        userRepositoryV2.getUserForSA();
    }
}
