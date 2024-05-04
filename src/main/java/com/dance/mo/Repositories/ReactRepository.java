package com.dance.mo.Repositories;

import com.dance.mo.Entities.ForumPost;
import com.dance.mo.Entities.React;
import com.dance.mo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactRepository extends JpaRepository<React, Long>  {

    List<React> findByForumPost(ForumPost forumPost);
    React findByUserAndForumPost(User u,ForumPost  p);

    @Query("SELECT r.forumPost FROM React r JOIN r.forumPost f GROUP BY f.postId ORDER BY COUNT(r) DESC")
    List<ForumPost> compterTotalLikesReacts(List<ForumPost> forumPosts);
}
