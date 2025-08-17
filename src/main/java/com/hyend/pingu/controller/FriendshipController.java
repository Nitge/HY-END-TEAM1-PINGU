package com.hyend.pingu.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyend.pingu.dto.FriendshipDTO;
import com.hyend.pingu.service.FriendshipService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friendships")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping
    public ResponseEntity<Long> request(@RequestParam Long requesterId,
                                        @RequestParam Long receiverId) {
        return ResponseEntity.ok(friendshipService.request(requesterId, receiverId));
    }

    @PutMapping
    public ResponseEntity<Long> accept(@RequestParam Long requesterId,
                                        @RequestParam Long receiverId) {
        return ResponseEntity.ok(friendshipService.accept(requesterId, receiverId));
    }

    @DeleteMapping
    public ResponseEntity<Long> delete(@RequestParam Long requesterId,
                                        @RequestParam Long receiverId) {
        return ResponseEntity.ok(friendshipService.delete(requesterId, receiverId));
    }

    
    @GetMapping("/accepted/{userId}")
    public ResponseEntity<List<FriendshipDTO>> findFriendsAccepted(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.findFriendsAccepted(userId));
    }

   
    @GetMapping("/received-requests/{userId}")
    public ResponseEntity<List<FriendshipDTO>> findReceivedRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.findReceivedRequests(userId));
    }

    
    @GetMapping("/sent-requests/{userId}")
    public ResponseEntity<List<FriendshipDTO>> findSentRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.findSentRequests(userId));
    }

}
