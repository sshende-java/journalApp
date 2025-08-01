package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;      //add values in .yml file to get bean

    public void sendEmail(String to, String subject, String body) {
        try {

            //construct mail
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);

            //send mail
            javaMailSender.send(simpleMailMessage);

        } catch (Exception e) {
            log.error("Error while sending mail ", e);
        }
    }

}
