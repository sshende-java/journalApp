package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

//This class we are referring for Criteria  API

@Component
public class UserRepositoryV2 {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUserForSA() {       //it will get all user for Sentiment Analysis
        Query query = new Query();
//        query.addCriteria(Criteria.where("userName").is("ram"));    //where username is ram
//        query.addCriteria(Criteria.where("age").gte(20));           //where age >=20
//        query.addCriteria(Criteria.where("email").exists(true).and("sentimentAnalysis").is(true));
//        query.addCriteria(Criteria.where("roles").in("USER","ADMIN"));

        query.addCriteria(Criteria.where("email").exists(true).ne(null).ne(""));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}
