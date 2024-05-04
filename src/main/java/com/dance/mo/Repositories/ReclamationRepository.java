package com.dance.mo.Repositories;

import com.dance.mo.Entities.Reclamation;
import com.dance.mo.Entities.Role;
import com.dance.mo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    List<Reclamation> findByResult(String notResolved);
    List<Reclamation> findByUser(User user);

    long countByStatus(String status);

    @Query("SELECT r.user.userId, r.reclamationDate, COUNT(r) " +
            "FROM Reclamation r " +
            "GROUP BY r.user.userId, r.reclamationDate")
    List<Object[]> countReclamationsPerUser();

}
