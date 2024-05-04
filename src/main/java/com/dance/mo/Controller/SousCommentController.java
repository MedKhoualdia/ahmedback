package com.dance.mo.Controller;

import com.dance.mo.Entities.SousComment;
import com.dance.mo.Services.SousCommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/sous-comments")
@AllArgsConstructor
@CrossOrigin("*")
public class SousCommentController {

     SousCommentService sousCommentService;



    @PostMapping("/add/{parentCommentId}/{userId}")
    public SousComment addSousCommentToParentComment(@PathVariable Long parentCommentId,@PathVariable Long userId, @RequestBody SousComment sousComment) {
        return sousCommentService.addSousCommentToComment(parentCommentId,userId, sousComment);
    }

    @GetMapping("getall/{parentCommentId}")
    public List<SousComment> getAllSousCommentsForParentComment(@PathVariable Long parentCommentId) {
        return sousCommentService.getAllSousCommentsForParentComment(parentCommentId);
    }

    @PutMapping("/update")
    public SousComment updateSousComment( @RequestBody SousComment updatedSousComment) {
        return sousCommentService.updateSousComment( updatedSousComment);
    }

    @DeleteMapping("/delete/{sousCommentId}")
    public void deleteSousComment(@PathVariable Long sousCommentId) {
        sousCommentService.deleteSousComment(sousCommentId);
    }
}
