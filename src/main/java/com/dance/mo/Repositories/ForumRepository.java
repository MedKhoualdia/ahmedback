package com.dance.mo.Repositories;

import com.dance.mo.Entities.Comment;
import com.dance.mo.Entities.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumRepository extends JpaRepository<ForumPost, Long> {

    List<ForumPost> findByTitleContainingIgnoreCase(String keyword);
    @Query("SELECT fp FROM ForumPost fp WHERE UPPER(CAST(fp.postContent AS text)) LIKE %:keyword%")
    List<ForumPost> findByPostContentIgnoreCaseContaining(@Param("keyword") String keyword);

    List<ForumPost> findAllByOrderByPostDateDesc();

    List<ForumPost> findByPostCreatorUserId(Long userId);
    @Query("SELECT (f.comments) FROM ForumPost f WHERE f.postId = :postId")
    List<Comment> findAllByPostId(Long postId);



    /* @Query("SELECT count (f.reacts) FROM ForumPost f WHERE f.postId = :postId")
    int CompterTotalReacts(@Param("postId") Long postId);

    @Query("SELECT count (r.dislike) FROM ForumPost f join f.reacts r WHERE f.postId = :postId ")
    int CompterTotalLikesReacts(@Param("postId") Long postId) ;*/

}
