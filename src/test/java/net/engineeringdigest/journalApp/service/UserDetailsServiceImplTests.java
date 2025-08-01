package net.engineeringdigest.journalApp.service;

import com.mongodb.assertions.Assertions;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

//here i dont want to create the whole application context so i didnt user @SpringBootTest, we only need @InjectMocks and @Mock


public class UserDetailsServiceImplTests {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;      //this will not work unless you initialize mocks in @BeforeEach

    @BeforeEach         //executes before each @Test
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void loadUserByUsernameTest() {
        when(userRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(User.builder().userName("ram").password("sdfasdf").roles(new ArrayList<>()).build());
        UserDetails userDetails = userDetailsService.loadUserByUsername("xyz");
        Assertions.assertNotNull(userDetails);
    }

}
