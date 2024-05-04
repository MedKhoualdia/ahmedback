package com.dance.mo.Repositories;

import com.dance.mo.Entities.Comment;
import com.dance.mo.Entities.ReactComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactCommentRepository extends JpaRepository<ReactComment, Long> {
    List<ReactComment> findAllByComment(Comment comment);
}
