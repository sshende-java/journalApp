package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntity;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional      //will start a separate transaction in separate spring controlled transaction context   (ACID)
    public void saveEntry(JournalEntity journalEntity, String userName) {
        //fetch user
        User userInDb = userService.findByUserName(userName);

        journalEntity.setDate(LocalDateTime.now());

        //save journal to journal_entries
        JournalEntity savedEntry = journalEntryRepository.save(journalEntity);

        //now add same journal entry to user
        if (userInDb.getJournalEntities() == null) {
            userInDb.setJournalEntities(new ArrayList<>());
        }
        userInDb.getJournalEntities().add(savedEntry);

        //save User
        userService.saveEntryForExistingUser(userInDb);
    }

    public List<JournalEntity> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntity> getById(ObjectId myId) {
        return journalEntryRepository.findById(myId);
    }

    @Transactional
    public boolean deleteById(ObjectId myId, String userName) {
        //fetch user
        boolean isRemoved = false;
        try {
            User userInDb = userService.findByUserName(userName);

            isRemoved = userInDb.getJournalEntities().removeIf(x -> x.getId().equals(myId));

            if (isRemoved) {
                userService.saveEntryForExistingUser(userInDb);
                journalEntryRepository.deleteById(myId);
            }
            return isRemoved;
        } catch (Exception e) {
            log.info("Error", e);
            throw new RuntimeException("Error occured while dleelting entry " + e);
        }


    }


}
