package com.dance.mo.Services;

import com.dance.mo.Entities.Comment;
import com.dance.mo.Entities.SousComment;
import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.CommentRepository;
import com.dance.mo.Repositories.SousCommentRepository;
import com.dance.mo.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class SousCommentService {

    SousCommentRepository sousCommentRepository;

    CommentRepository commentRepository;
    UserRepository userRepository;
    // Create a new sous comment for a parent comment
    public SousComment addSousCommentToComment(Long commentId,Long userId, SousComment sousComment) {
        Comment comment = commentRepository.findById(commentId).get();

        User user=userRepository.findById(userId).get();
        sousComment.setCommentDate(new Date());


        sousComment.setUser(user);
        sousComment.setComment(comment);

        return sousCommentRepository.save(sousComment);
    }

    // Get all sous comments for a parent comment
    public List<SousComment> getAllSousCommentsForParentComment(Long parentCommentId) {
        return sousCommentRepository.findByComment_CommentId(parentCommentId);
    }




    // Get all sous comments for a parent comment
// Update a sous comment
    public SousComment updateSousComment( SousComment updatedSousComment) {
        SousComment sc=sousCommentRepository.findById(updatedSousComment.getScommentId()).get();
         User u=sc.getUser();
         Comment c =sc.getComment();
         updatedSousComment.setUser(u);
         updatedSousComment.setComment(c);
        return sousCommentRepository.save(updatedSousComment);
    }


    // Delete a sous comment

    public void deleteSousComment(Long sousCommentId) {
        SousComment c=sousCommentRepository.findById(sousCommentId).get();
        c.setUser(null);
        c.setComment(null);
        sousCommentRepository.deleteById(sousCommentId);
    }

    }


