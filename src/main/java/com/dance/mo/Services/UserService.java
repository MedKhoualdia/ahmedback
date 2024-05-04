package com.dance.mo.Services;

import com.dance.mo.Entities.Role;
import com.dance.mo.Entities.User;
import com.dance.mo.Exceptions.UserException;
import com.dance.mo.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {


    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public User addUser(User user) {
        User UserExist = userRepository.getUserByEmail(user.getEmail());
        if (UserExist != null) {
            throw new UserException("User already exist");
        }
        User newUser = new User();
        newUser.setUserId(user.getUserId());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setBirthday(user.getBirthday());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setRole(user.getRole());
        newUser.setEnabled(true);
        /*if (user.getImage() != null){
            newUser.setImage(Base64.getDecoder().decode(user.getImage()));
        }*/
        return userRepository.save(newUser);

    }

    //find all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    ///find by id
    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserException("User not found with id: " + userId);
        }
    }

    public Boolean deleteUser(Long userId) {
        Optional<User> userOptional2 = userRepository.findById(userId);
        if (userOptional2.isPresent()) {
            userRepository.deleteById(userId);
            return true;
        } else {
            return false;
        }
    }


    public List<User> getRoleUsers(){return  userRepository.findUserByRole(Role.USER);}

    public User updateUser(Long userId, User updateuser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with id: " + userId));
        user.setFirstName(updateuser.getFirstName());
        user.setLastName(updateuser.getLastName());
        user.setEmail(updateuser.getEmail());
        user.setBirthday(updateuser.getBirthday());
        user.setPhoneNumber(updateuser.getPhoneNumber());
        user.setPassword(updateuser.getPassword());
        user.setRole(updateuser.getRole());
        user.setEnabled(updateuser.isEnabled());

        // Save the updated user
        return userRepository.save(user);
    }
    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }
    public List<User> getFriendList(User currentUser) {
        return currentUser.getFriends();
    }




}






