package com.dance.mo.Services;

import com.dance.mo.Entities.Comment;
import com.dance.mo.Entities.ForumPost;
import com.dance.mo.Entities.SousComment;
import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.CommentRepository;
import com.dance.mo.Repositories.ForumRepository;
import com.dance.mo.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class CommentService {

    CommentRepository commentRepository;
    ForumRepository forumPostRepository;
    UserRepository userRepository;
    // Create a new comment for a forum post
    public Comment addCommentToPost(Long postId,Long userId, Comment comment) {
        ForumPost forumPost = forumPostRepository.findById(postId).get();
        User user=userRepository.findById(userId).get();
        comment.setCommentDate(new Date());
        comment.setForumPost(forumPost);

        comment.setUser(user);
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long postId) {
        return forumPostRepository.findAllByPostId(postId);

    }

    public Comment updateComment( Comment updatedComment) {
         Long id = updatedComment.getCommentId();
         Comment c=commentRepository.findById(id).get();
        ForumPost v= c.getForumPost();

         updatedComment.setForumPost(v);
         updatedComment.setCommentDate(new Date());
         updatedComment.setUser(c.getUser());
        return commentRepository.save(updatedComment);
    }

    public void deleteComment(Long commentId) {

        Comment c=commentRepository.findById(commentId).get();
        c.setUser(null);

        for(SousComment s:c.getSousComments()){
            s.setComment(null);

        }
        commentRepository.delete(c);
    }
}
