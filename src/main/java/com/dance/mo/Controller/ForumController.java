package com.dance.mo.Controller;

import com.dance.mo.Entities.ForumPost;
import com.dance.mo.Services.ForumService;
import com.dance.mo.Services.ReactService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/Forum")
@AllArgsConstructor
public class ForumController {


    ForumService forumService;
    ReactService reactService;
    //getAllForumPost
    @GetMapping("/allForumPost")
    public List<ForumPost> getAllForumPost() {
        return forumService.getallForumPost();

    }

    //addForum
    @PostMapping("/addForumPost/{userId}")
    public ResponseEntity<String> addPost(@RequestBody ForumPost forumpost,@PathVariable Long userId) {

        return forumService.addForumPost(forumpost,userId);
    }
    @PostMapping("/addForumPostToFacebook")
    public ResponseEntity<String> addForumPostToFacebook(@RequestBody String message) {
        return forumService.addForumPostToFacebook(message);
    }
    //update
    @PutMapping("/updateForumPost")
    public ResponseEntity<ForumPost> updateForumPost(@RequestBody ForumPost updatePost) {
        // Retrieve the existing ForumPost from the database
        ForumPost existingPost = forumService.getForumPostById(updatePost.getPostId());

        // Check if the ForumPost exists
        if (existingPost != null) {
            // Update the properties of the existing ForumPost with the properties of the updatePost
            existingPost.setTitle(updatePost.getTitle());
            existingPost.setPostContent(updatePost.getPostContent());
            existingPost.setImageUrl(updatePost.getImageUrl());

            // Call the service method to update the ForumPost
            ForumPost updatedPost = forumService.updateForumPost(existingPost);

            // Check if the update was successful
            if (updatedPost != null) {
                // If updated successfully, return the updated ForumPost with HTTP status OK
                return ResponseEntity.ok(updatedPost);
            } else {
                // If the update operation failed for some reason, return HTTP status Internal Server Error
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            // If the ForumPost with the given ID was not found, return HTTP status Not Found
            return ResponseEntity.notFound().build();
        }
    }

    //getForumById
    @GetMapping("/getForumPostById/{postId}")
    public ResponseEntity<ForumPost> getForumPostById(@PathVariable Long postId) {
        ForumPost forumPost = forumService.getForumPostById(postId);
        if (forumPost != null) {
            return ResponseEntity.ok(forumPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("/deleteFOrumPost/{postId}")
    public void deleteForumPost(@PathVariable Long postId) {
        forumService.deleteForumPostById(postId);


    }

    @GetMapping("/user/{userId}")
    public List<ForumPost> getForumPostsByUser(@PathVariable Long userId) {
        return forumService.getForumPostsByUser(userId);
    }

    @GetMapping("/countComments/{postId}")
    public ResponseEntity<Long> countCommentsForPost(@PathVariable Long postId) {
        Long commentCount = forumService.countCommentsForPost(postId);
        if (commentCount != null) {
            return ResponseEntity.ok(commentCount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<ForumPost> searchForumPosts(@RequestParam String keyword) {
        return forumService.searchForumPosts(keyword);
    }

    @GetMapping("/newestPostById")
    public ResponseEntity<ForumPost> getNewestForumPostById() {
        ForumPost newestPost = forumService.getNewestForumPostById();
        if (newestPost != null) {
            return ResponseEntity.ok(newestPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    @PostMapping("/upload")
    public ResponseEntity<String> handleImageUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String imageUrl = forumService.uploadImage(file);
        return ResponseEntity.ok(imageUrl);

    }
    @GetMapping("/getUrl/{imageName:.+}")
    public ResponseEntity<String> getImageUrl(@PathVariable String imageName) {
        String imageUrl = forumService.getImageUrl(imageName);
        return ResponseEntity.ok().body(imageUrl);
    }
}
