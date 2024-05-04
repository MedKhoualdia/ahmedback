package com.dance.mo.Repositories;

import com.dance.mo.Entities.Role;
import com.dance.mo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    User getUserByEmail(String email);

    List<User> findUserByRole(Role role);

    @Query("SELECT u.profileImage FROM User u WHERE u.email = :email")
    byte[] findUserFaceImageByEmail(String email);





}
