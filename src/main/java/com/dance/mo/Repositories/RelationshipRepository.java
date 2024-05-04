package com.dance.mo.Repositories;

import com.dance.mo.Entities.Enumarations.RelationshipStatus;
import com.dance.mo.Entities.Relationship;
import com.dance.mo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    Relationship findBySenderAndReceiver(User sender, User receiver);
    List<Relationship> findByReceiverAndStatus(User receiver, RelationshipStatus status);
    Relationship findBySenderAndReceiverAndStatus(User sender, User receiver, RelationshipStatus status);


}
