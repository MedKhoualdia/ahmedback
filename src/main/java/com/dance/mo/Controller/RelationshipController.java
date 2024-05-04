package com.dance.mo.Controller;

import com.dance.mo.Entities.Enumarations.RelationshipStatus;
import com.dance.mo.Entities.Relationship;
import com.dance.mo.Entities.User;
import com.dance.mo.Services.RelationshipService;
import com.dance.mo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relationships")
public class RelationshipController {

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private UserService userService;
    @GetMapping("/pending/{receiverId}")
    public ResponseEntity<List<Relationship>> getPendingFriendRequests(@PathVariable Long receiverId) {
        User user = userService.getUserById(receiverId);
        List<Relationship> pendingRequests = relationshipService.getPendingFriendRequests(user);
        return ResponseEntity.ok(pendingRequests);
    }
    @PostMapping("/friend-request/{senderId}/{receiverId}")
    public ResponseEntity<Void> sendFriendRequest(@PathVariable Long senderId, @PathVariable String receiverId) {
        User sender = userService.getUserById(senderId);
        User receiver = userService.getUserByEmail(receiverId);

        relationshipService.sendFriendRequest(sender, receiver);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept/{senderId}/{receiverId}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long senderId, @PathVariable Long receiverId) {
        User sender = userService.getUserById(senderId);
        User receiver = userService.getUserById(receiverId);
        relationshipService.acceptFriendRequest(sender, receiver);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/block/{senderId}/{receiverId}")
    public ResponseEntity<Void> blockUser(@PathVariable Long senderId, @PathVariable String  receiverId) {
        User currentUser = userService.getUserById(senderId);
        User userToBlock = userService.getUserByEmail(receiverId);

        relationshipService.blockUser(currentUser, userToBlock);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unblock/{senderId}/{receiverId}")
    public ResponseEntity<Void> unblockUser(@PathVariable Long senderId, @PathVariable String receiverId) {
        User currentUser = userService.getUserById(senderId);
        User userToUnblock = userService.getUserByEmail(receiverId);

        relationshipService.unblockUser(currentUser, userToUnblock);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/status/{currentUserId}/{otherUserId}")
    public ResponseEntity<RelationshipStatus> checkRelationshipStatus(@PathVariable Long currentUserId, @PathVariable String otherUserId) {
        User currentUser = userService.getUserById(currentUserId);
        User userToUnblock = userService.getUserByEmail(otherUserId);
        RelationshipStatus status = relationshipService.checkRelationshipStatus(currentUser, userToUnblock);
        return ResponseEntity.ok(status);
    }
    @GetMapping("/can-send-message/{senderId}/{receiverId}")
    public ResponseEntity<Boolean> canSendMessage(@PathVariable Long senderId, @PathVariable String receiverId) {
        User sender = userService.getUserById(senderId);
        User receiver = userService.getUserByEmail(receiverId);

        boolean canSend = relationshipService.canSendMessage(sender, receiver);
        return ResponseEntity.ok(canSend);
    }
}