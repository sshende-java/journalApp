package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntity;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        //auth
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.findByUserName(userName);

        List<JournalEntity> all = user.getJournalEntities();

        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("id/{idToFind}")
    public ResponseEntity<JournalEntity> getJournalById(@PathVariable ObjectId idToFind) {
        //auth
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        //get user from db
        User userInDb = userService.findByUserName(userName);

        //check if user in db has idToFind
        List<JournalEntity> collect = userInDb.getJournalEntities().stream().filter(x -> x.getId().equals(idToFind)).collect(Collectors.toList());

        if (!collect.isEmpty()) {
            Optional<JournalEntity> optionalJournalEntity = journalEntryService.getById(idToFind);
            if (optionalJournalEntity.isPresent()) {
                return new ResponseEntity<>(optionalJournalEntity.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping
    public ResponseEntity<JournalEntity> createEntry(@RequestBody JournalEntity myEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            journalEntryService.saveEntry(myEntry, userName);

            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @DeleteMapping("id/{idToDelete}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId idToDelete) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();


        boolean isRemoved = journalEntryService.deleteById(idToDelete, userName);
        if (isRemoved)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @PutMapping("id/{myId}")
//    public JournalEntity updateJournalById(@PathVariable ObjectId myId, @RequestBody JournalEntity newEntry) {
//        JournalEntity oldEntry = journalEntryService.getById(myId).orElse(null);
//        if (oldEntry != null) {
//            oldEntry.setTitle(newEntry.getTitle() != null ? newEntry.getTitle() : oldEntry.getTitle());
//            oldEntry.setContent(newEntry.getContent() != null ? newEntry.getContent() : oldEntry.getContent());
//            journalEntryService.saveEntry(oldEntry, user);
//            return oldEntry;
//        }
//        journalEntryService.saveEntry(newEntry, user);
//        return newEntry;
//    }

}
