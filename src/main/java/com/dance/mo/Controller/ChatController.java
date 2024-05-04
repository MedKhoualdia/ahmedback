package com.dance.mo.Controller;

import com.dance.mo.Entities.ExchangedMessages;
import com.dance.mo.Entities.Message;
import com.dance.mo.Entities.User;
import com.dance.mo.Services.ExchangedMessagesService;
import com.dance.mo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Objects;

@CrossOrigin("*")
@Controller
public class ChatController {
    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private ExchangedMessagesService exchangedMessagesService;
    public ChatController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receivePublicMessage(@Payload Message chatMessage) {
        return chatMessage;
    }
    @MessageMapping("/private-message/{session}")
    public void handlePrivateMessage(@Payload Message message, @DestinationVariable("session") String user) {
        User sender = userService.getUserByEmail(message.getSenderName());
        User receiver = userService.getUserByEmail(message.getReceiverName());


        Iterator<User> it = sender.getFriends().iterator();
        System.out.println(sender);
        System.out.println(receiver);
        while (it.hasNext()){
            System.out.println(it.next());
        }
        if (!sender.getFriends().stream().anyMatch(friend -> friend.getUserId().equals(receiver.getUserId()))) {
            throw new IllegalArgumentException("Cannot send messages to this user. They are not in your friend list.");
        }
        ExchangedMessages savedMessage = new ExchangedMessages();
        savedMessage.setContent(message.getMessage());
        savedMessage.setReceiver(receiver);
        savedMessage.setSender(sender);
        savedMessage.setSentTime(LocalDateTime.now());

        ExchangedMessages savedMessage1 = exchangedMessagesService.saveMessage(savedMessage);
        message.setId(savedMessage1.getId());
        System.out.println("Received message: " + message);
        System.out.println("Receiver name: " + message.getReceiverName());
        messagingTemplate.convertAndSendToUser(user, "/private", message);
    }


}
