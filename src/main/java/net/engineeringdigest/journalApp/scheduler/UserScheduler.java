package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.entity.JournalEntity;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepositoryV2;
import net.engineeringdigest.journalApp.service.EmailService;
import net.engineeringdigest.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryV2 userRepositoryV2;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;


    @Scheduled(cron = "0 0 9 * * SUN")      //every run every Sunday at 9 AM
    public void fetchUsersAndSendSentimentAnalysisMail() {

        //fetch users which has sentiment analysis
        List<User> users = userRepositoryV2.getUserForSA();

        //fetch journal entries of those users
        for (User user : users) {
            List<JournalEntity> journalEntries = user.getJournalEntities();

            //it will give entries of last week till today
            List<JournalEntity> filteredEntries = journalEntries.stream().filter(
                    x -> x.getDate().isAfter(
                            LocalDateTime.now().minus(7, ChronoUnit.DAYS)
                    )
            ).collect(Collectors.toList());

            List<String> contentsList = filteredEntries.stream().map(e -> e.getContent()).collect(Collectors.toList());
            String combinedContentsOfAllEntries = String.join(" ", contentsList);

            //serves no purpose as of now
            String sentiment = sentimentAnalysisService.getSentiment(combinedContentsOfAllEntries);

            emailService.sendEmail(user.getEmail(), "Sentiment for Last 7 days ", sentiment);

        }

    }

}
