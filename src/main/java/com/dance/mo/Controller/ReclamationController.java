package com.dance.mo.Controller;

import com.dance.mo.Entities.Notification;
import com.dance.mo.Entities.Reclamation;
import com.dance.mo.Entities.User;
import com.dance.mo.Services.NotificationService;
import com.dance.mo.Services.ReclamationService;
import com.dance.mo.Services.UserService;
import com.dance.mo.auth.DTO.ReclamationDTO;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/reclamations")
public class ReclamationController {
    ////Twilio
    @Value("${twilio.account.sid}")
    private String twilioAccountSid;
    @Value("${twilio.auth.token}")
    private String twilioAuthToken;
    @Value("${twilio.from.number}")
    private String twilioFromNumber;
    ///Twilio
    @Autowired
    private ReclamationService reclamationService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService ;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ReclamationController(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reclamation>> getAllReclamationsByUser(@PathVariable Long userId) {
        User user = new User();
        user.setUserId(userId);
        List<Reclamation> reclamations = reclamationService.getAllReclamationsByUser(user);
        return ResponseEntity.ok(reclamations);
    }
    @GetMapping
    public List<Reclamation> getAllReclamations() {
        return reclamationService.getAllReclamations();
    }
    @GetMapping("/reclamation/{Id}")
    public ResponseEntity<ReclamationDTO> getReclamationById(@PathVariable Long Id) {
        Reclamation rec = reclamationService.getReclamationById(Id);
        ReclamationDTO recDTO = mapReclamationToDTO(rec);
        return new ResponseEntity<>(recDTO, HttpStatus.ACCEPTED);
    }
    @PostMapping("/reclamation/resolve/{Id}")
    public ResponseEntity<ReclamationDTO> resolveReclamation(@PathVariable Long Id) {
        Reclamation rec = reclamationService.getReclamationById(Id);
        rec.setResult("resolved");
        Reclamation updated = reclamationService.createReclamation(rec);
        ReclamationDTO recDTO = mapReclamationToDTO(updated);
        Notification notification = new Notification();
        notification.setReceiver(rec.getUser());
        notification.setStatus(rec.getStatus());
        notification.setMessage("Your reclamation has been resolved.");
        notification.setSendDate(Date.valueOf(rec.getReclamationDate()));
        notification.setReclamationId(rec.getId());
        notification.setSeen(false);
        String userId = rec.getUser().getUserId().toString();
        String destination = "/topic/notifications/" + userId;
        notificationService.sendNotification(notification);
        messagingTemplate.convertAndSend(destination, notification);
        return new ResponseEntity<>(recDTO, HttpStatus.ACCEPTED);
    }

    private ReclamationDTO mapReclamationToDTO(Reclamation rec) {
        ReclamationDTO recDTO = new ReclamationDTO();
        recDTO.setId(rec.getId());
        recDTO.setReclamationDate(rec.getReclamationDate());
        recDTO.setStatus(rec.getStatus());
        recDTO.setDescription(rec.getDescription());
        recDTO.setUserId(rec.getUser());
        recDTO.setResult(rec.getResult());
        recDTO.setScreenshot(rec.getScreenshot());
        return recDTO;
    }


    @PostMapping("/{userID}")
    public ResponseEntity<Reclamation> createReclamation(@PathVariable Long userID,@RequestBody Reclamation reclamation) {
        User user = userService.getUserById(userID);
        System.out.println(user.getEmail());
        reclamation.setUser(user);
        reclamation.setResult("Not resolved");
        Reclamation createdReclamation = reclamationService.createReclamation(reclamation);
        Notification notification = new Notification();
        User user2 = userService.getUserById(1L);
        notification.setReceiver(user2);
        notification.setStatus(reclamation.getStatus());
        notification.setMessage("A new reclamation has been created.");
        notification.setSendDate(Date.valueOf(reclamation.getReclamationDate()));
        notification.setReclamationId(createdReclamation.getId());
        notification.setSeen(false);
        notificationService.sendNotification(notification);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
        ////twilio
       /* var phonenum = user2.getPhoneNumber().toString();
        String messageBody = "Dear Admin,\n\n"
                + "A new reclamation has been created in the system . Here are the details:\n\n"
                + "- Reclamation Type: " + createdReclamation.getStatus() + "\n"
                + "- User: " + user.getFirstName() + " " + user.getLastName() + "\n"
                + "- User email: "  + user.getEmail() + "\n"
                + "- Date: " + createdReclamation.getReclamationDate() + "\n"
                + "- Description: " + createdReclamation.getDescription() + "\n\n"
                + "Please review the reclamation and take appropriate action.\n\n"
                + "Thank you.\n\n"
                + "Best regards,\n"
                + "DanceScape Explorer";
        Twilio.init(twilioAccountSid, twilioAuthToken);
        Message.creator(
                new PhoneNumber("+216"+ phonenum),
                new PhoneNumber(twilioFromNumber),
                messageBody
        ).create();*/
        ////twilio
        return new ResponseEntity<>(createdReclamation, HttpStatus.CREATED);
    }
}
