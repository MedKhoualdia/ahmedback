package com.dance.mo.Controller;

import com.dance.mo.Entities.Notification;
import com.dance.mo.Exceptions.UserException;
import com.dance.mo.Entities.User;
import com.dance.mo.Services.NotificationService;
import com.dance.mo.Services.UserService;
import org.aspectj.weaver.ast.Not;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;


import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    private NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    @GetMapping("/notifications")
    public List<Notification> getAllNotifications() {
        return notificationService.getLatestNotifications();
    }
    @PostMapping("/notifications/{notificationId}")
    public Notification openNotification(@PathVariable Long notificationId) {
        Notification notification = notificationService.getNotification(notificationId);
        notification.setSeen(true);
        return notificationService.saveNotification(notification);
    }
    ///ADD USER
    @PostMapping("/addUser")
    public ResponseEntity addUserResponse(@RequestBody User user){
     try {
         User addUser = userService.addUser(user);
         return new ResponseEntity<>(addUser, HttpStatus.CREATED);

     } catch (UserException e){
         logger.error("Error adding user: {}", e.getMessage());
         return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
     }
    }
    //GET ALL
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    // GET ONE
    @GetMapping("/getUserById/{userId}")
    public User getUserById(@PathVariable Long userId){
        return userService.getUserById(userId);
    }
    // DELETE
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        if (userService.deleteUser(userId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // UPDATE
    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        try {
            User user = userService.updateUser(userId, updatedUser);
            return ResponseEntity.ok().body(user);
        } catch (UserException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/roleUser")
    public ResponseEntity<List<User>> getUsersWithUserRole() {
        List<User> usersWithUserRole = userService.getRoleUsers();
        return ResponseEntity.ok(usersWithUserRole);
    }



}
