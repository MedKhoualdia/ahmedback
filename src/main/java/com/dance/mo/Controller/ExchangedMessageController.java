package com.dance.mo.Controller;
import com.dance.mo.Entities.ExchangedMessages;
import com.dance.mo.Entities.User;
import com.dance.mo.Services.ExchangedMessagesService;
import com.dance.mo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/ExchangedMessages")
public class ExchangedMessageController {
    @Autowired
    private ExchangedMessagesService exchangedMessagesService;
    @Autowired
    private UserService userService;
    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<User>> getFriendsList(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<User> friendsList = userService.getFriendList(user);
        return ResponseEntity.ok(friendsList);
    }
    @GetMapping("/exchanged/{userId}/{receiverId}")
    public ResponseEntity<List<ExchangedMessages>> getExchangedMessages(@PathVariable String userId, @PathVariable String receiverId) {
        try {
            User sender = userService.getUserByEmail(userId);
            User receiver = userService.getUserByEmail(receiverId);

            // Ensure sender and receiver are not null
            if (sender == null || receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Fetch messages
            List<ExchangedMessages> sentMessages = exchangedMessagesService.findBySenderAndReceiver(sender, receiver);
            List<ExchangedMessages> receivedMessages = exchangedMessagesService.findByReceiverAndSender(receiver, sender);

            // Combine and sort messages
            List<ExchangedMessages> exchangedMessages = new ArrayList<>(sentMessages);
            exchangedMessages.addAll(receivedMessages);
            exchangedMessages.sort(Comparator.comparing(ExchangedMessages::getSentTime));

            // Log the first message's content
            if (!exchangedMessages.isEmpty()) {
                System.out.println("First message content: " + exchangedMessages.get(0).getContent());
            }

            // Return OK response with messages
            return ResponseEntity.ok(exchangedMessages);
        } catch (Exception e) {
            // Log error and return internal server error response
            System.err.println("Error fetching exchanged messages: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/delete/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        try {
            // Get the message by its ID
            Optional<ExchangedMessages> message = exchangedMessagesService.findById(messageId);

            // Check if the message exists
            if (message.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Update the content of the message to "This message has been deleted"
            message.get().setContent("This message has been deleted");

            // Save the changes
            exchangedMessagesService.saveMessage(message.get());

            // Return no content response to indicate successful deletion
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Log error and return internal server error response
            System.err.println("Error deleting message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
