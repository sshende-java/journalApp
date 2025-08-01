package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {


    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;


    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean saveEntry(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User user1 = userRepository.findByUserName(user.getUserName());     //check if user already exists
            if (user1 == null) {
                userRepository.save(user);
                return true;
            } else {
                logger.info("user already exists with name {}",user1.getUserName());
                return false;
            }
        } catch (Exception e) {
            logger.info("error here {}", e);
            logger.warn("error here {}", e);
            logger.error("error here {}", e);
            logger.debug("error here {}", e);   //not enabled by default
            logger.trace("error here {}", e);   //not enabled by default
            return false;
        }
    }

    public void saveEntryForExistingUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(ObjectId myId) {
        return userRepository.findById(myId);
    }

    public void deleteById(ObjectId myId) {
        userRepository.deleteById(myId);

    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    public void deleteUserByName(String name) {
        userRepository.deleteByUserName(name);
    }

    public void saveAdmin(User user) {
        user.setRoles(new ArrayList<>(Arrays.asList("User", "Admin")));      //Spring security needs one Dummy Role, if u dont want create your own class which implements UserDetails.java interface
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
