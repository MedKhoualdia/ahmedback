package com.dance.mo.Repositories;

import com.dance.mo.Entities.SousComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  SousCommentRepository extends JpaRepository<SousComment, Long> {
    List<SousComment> findByComment_CommentId(Long parentCommentId);
}
