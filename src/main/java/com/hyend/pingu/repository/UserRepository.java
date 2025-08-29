package com.hyend.pingu.repository;

import com.hyend.pingu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM User u WHERE u.name like :username and u.password like :password", nativeQuery = true)
    Optional<User> findByUsernameAndPassword(
            @Param("username") String username, @Param("password") String password);
}
