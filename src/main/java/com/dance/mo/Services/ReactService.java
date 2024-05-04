package com.dance.mo.Services;

import com.dance.mo.Entities.ForumPost;
import com.dance.mo.Entities.React;
import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.ForumRepository;
import com.dance.mo.Repositories.ReactRepository;
import com.dance.mo.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
@Slf4j

public class ReactService {

     ReactRepository reactRepository;
    ForumRepository forumPostRepository;
    UserRepository userRepository;

    @Transactional
    public React addReactLikeToForumPost(Long postId, Long userId, React react) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Forum post not found with id: " + postId));
        React existingReact = reactRepository.findByUserAndForumPost(user, post);
        if (existingReact != null) {

            if (existingReact.isLiked()) {


                    existingReact.setLiked(false);
                    existingReact.setDislike(true);

            } else if (existingReact.isDislike()) {

                    existingReact.setDislike(false);
                    existingReact.setLiked(true);

            }
            return reactRepository.save(existingReact);
        } else {
            react.setUser(user);
            react.setForumPost(post);


                react.setLiked(true);


            // Save the new react
            return reactRepository.save(react);
        }
    }


    @Transactional
    public React addReactDislikeToForumPost(Long postId, Long userId, React react) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Forum post not found with id: " + postId));

        React existingReact = reactRepository.findByUserAndForumPost(user, post);
        if (existingReact != null) {
            // User has already reacted to the post
            throw new IllegalStateException("User has already disliked this post");
        }

        // Proceed to add the dislike
        react.setUser(user);
        react.setLiked(false);
        react.setDislike(true);
        react.setForumPost(post);
        return reactRepository.save(react);
    }


    public List<React> getAllReactsForForumPost(Long postId) {
        // Find the ForumPost by its ID
        Optional<ForumPost> forumPostOptional = forumPostRepository.findById(postId);
        if (forumPostOptional.isPresent()) {
            ForumPost forumPost = forumPostOptional.get();
            // Retrieve Reacts associated with the ForumPost
            return reactRepository.findByForumPost(forumPost);
        } else {
            throw new IllegalArgumentException("ForumPost not found with ID: " + postId);
        }
    }
    public List<ForumPost> TopPost(){

        List<ForumPost> posts= forumPostRepository.findAll();
        List<ForumPost> f =reactRepository.compterTotalLikesReacts(posts);
     log.info("ljhhgjj"+f);
        return f;
    }
/*
    public int affichernbLikes(Long postId){
       int a= forumPostRepository.CompterTotalLikesReacts(postId);
        return a;
    }*/
    }




