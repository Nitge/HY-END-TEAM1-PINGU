package com.hyend.pingu.repository;

import com.hyend.pingu.entity.Friendship;
import com.hyend.pingu.enumeration.Status;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {


        boolean existsByFriend1_IdAndFriend2_Id(Long friend1Id, Long friend2Id);

        
        @Query("SELECT f FROM Friendship f " +
                "WHERE (f.friend1.id = :friend1Id AND f.friend2.id = :friend2Id) " +
                "OR (f.friend1.id = :friend2Id AND f.friend2.id = :friend1Id)")
        Optional<Friendship> findFriendship(@Param("friend1Id") Long friend1Id,
                                                @Param("friend2Id") Long friend2Id);

        @Query("SELECT f FROM Friendship f " +
                "WHERE ((f.friend1.id = :friend1Id AND f.friend2.id = :friend2Id) " +
                "   OR (f.friend1.id = :friend2Id AND f.friend2.id = :friend1Id)) " +
                "AND f.status = :status")
        Optional<Friendship> findFriendshipWithStatus(@Param("friend1Id") Long friend1Id,
                                              @Param("friend2Id") Long friend2Id,
                                              @Param("status") Status status);
                                                
        @Query("SELECT f FROM Friendship f " +
                "WHERE (f.friend1.id = :userId OR f.friend2.id = :userId) " +
                "AND f.status = com.hyend.pingu.enumeration.Status.ACCEPTED")
        List<Friendship> findFriendsAccepted(@Param("userId") Long userId);
                
        @Query("SELECT f FROM Friendship f " +
                "WHERE f.friend2.id = :userId " +
                "AND f.status = com.hyend.pingu.enumeration.Status.REQUESTING")
        List<Friendship> findReceivedRequests(@Param("userId") Long userId);

        @Query("SELECT f FROM Friendship f " +
                "WHERE f.friend1.id = :userId " +
                "AND f.status = com.hyend.pingu.enumeration.Status.REQUESTING")
        List<Friendship> findSentRequests(@Param("userId") Long userId);


 }                                       
