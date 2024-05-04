package com.dance.mo.Controller;

import com.dance.mo.Entities.React;
import com.dance.mo.Services.ForumService;
import com.dance.mo.Services.ReactService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reacts")
@AllArgsConstructor
@CrossOrigin("*")
public class ReactController {

    private ForumService forumService;

    private  ReactService reactService;

    @PostMapping("/like/{postId}/{idUser}")
    public React addLikeReactToForumPost(@PathVariable Long postId,@PathVariable  Long idUser, @RequestBody React react) {
        return reactService.addReactLikeToForumPost(postId,idUser,react);

    }
    @PostMapping("/dislike/{postId}/{idUser}")
    public ResponseEntity<?> adddisLikeReactToForumPost(@PathVariable Long postId,@PathVariable  Long idUser, @RequestBody React react) {

            // Add a like react to the forum post
            React addedReact = reactService.addReactDislikeToForumPost(postId,idUser, react);
            return ResponseEntity.ok(addedReact);

    }

    // Retrieve all reacts for a forum post
    /*
    @GetMapping("reacts/{postId}")
    public int getAllReactsForForumPost(@PathVariable Long postId) {
       return reactService.affichernbLikes(postId);
    }*/

}
