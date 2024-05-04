package com.dance.mo.Controller;

import com.dance.mo.Entities.Comment;
import com.dance.mo.Services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/comments")
@CrossOrigin("*")
public class CommentController {

   private    CommentService commentService;



    @PostMapping("/addCommentToPost/{postId}/{userId}")
    public Comment addCommentToPost(@PathVariable Long postId,@PathVariable Long userId, @RequestBody Comment comment) {
        return commentService.addCommentToPost(postId,userId, comment);
    }

    @GetMapping("/getAllCommentsForPost/{postId}")
    public List<Comment> getAllCommentsForPost(@PathVariable Long postId) {
        return commentService.getAllCommentsForPost(postId);
    }

    @PutMapping("/updateComment")
    public Comment updateComment( @RequestBody Comment updatedComment) {
        return commentService.updateComment( updatedComment);
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }



    }
