package com.hyend.pingu.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.hyend.pingu.dto.FriendshipResponseDTO;
import com.hyend.pingu.dto.FriendshipRequestDTO;
import com.hyend.pingu.service.FriendshipService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friendships")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping
    public ResponseEntity<Long> request(@RequestBody FriendshipRequestDTO friendshipRequestDTO) {
    Long id = friendshipService.request(
        friendshipRequestDTO.getRequesterId(), 
        friendshipRequestDTO.getReceiverId());
    return new ResponseEntity<>(id, HttpStatus.CREATED);
}

    @PutMapping
    public ResponseEntity<Long> accept(@RequestBody FriendshipRequestDTO friendshipRequestDTO) {
        return ResponseEntity.ok(friendshipService.accept(
            friendshipRequestDTO.getRequesterId(), 
            friendshipRequestDTO.getReceiverId()));
    }

    @DeleteMapping("/{requesterId}/{receiverId}")
    public ResponseEntity<Long> delete(@PathVariable Long requesterId,
                                        @PathVariable Long receiverId) {
        return ResponseEntity.ok(friendshipService.delete(requesterId, receiverId));
    }

    
    @GetMapping("/accepted/{userId}")
    public ResponseEntity<List<FriendshipResponseDTO>> findFriendsAccepted(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.findFriendsAccepted(userId));
    }

   
    @GetMapping("/received-requests/{userId}")
    public ResponseEntity<List<FriendshipResponseDTO>> findReceivedRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.findReceivedRequests(userId));
    }

    
    @GetMapping("/sent-requests/{userId}")
    public ResponseEntity<List<FriendshipResponseDTO>> findSentRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.findSentRequests(userId));
    }

}
