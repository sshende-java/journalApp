package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.repository.UserRepository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//this @SpringBootTest will actually create application context and load @Component , if you dont want that behavior then u can refer UserDetailsServiceImplTest.java
//here combination will be @SpringBootTest , @Autowired and @MockBean, Autowired will go on class which u r trying to test and @MockBean will go an dependencies of that test class

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;


    @Disabled       //dont run for now
    @Test
    public void test1() {
        assertEquals(4, 2 + 2);
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "ram",
            "shyam",
            "ghan"
    })
    public void testFindByUserName(String name) {
        assertNotNull(userRepository.findByUserName(name));
    }


    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,10,12",
            "3,3,9"
    })
    public void test2(int a, int b, int expected) {
        assertEquals(expected, a + b);
    }


//    @ParameterizedTest
//    @ArgumentsSource(UserArgumentsProvider.class)
//    public void testSaveNewUser(User user){
//        assertTrue(userService.saveNewUser(user));
//    }

}
