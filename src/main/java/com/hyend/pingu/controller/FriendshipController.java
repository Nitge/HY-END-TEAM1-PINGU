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

/**
 * FriendshipController.java
 * 친구 관계(요청, 수락, 삭제, 조회)와 관련된 API 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/friendships") // 이 컨트롤러의 모든 API는 '/friendships' 경로로 시작됩니다.
public class FriendshipController {

    private final FriendshipService friendshipService;

    /**
     * 다른 사용자에게 친구 요청을 보냅니다.
     * @param friendshipRequestDTO 요청자(requesterId)와 수신자(receiverId)의 ID를 담은 DTO.
     * @return 생성된 친구 관계 데이터의 ID를 반환합니다.
     */
    @PostMapping // HTTP POST /friendships
    public ResponseEntity<Long> request(@RequestBody FriendshipRequestDTO friendshipRequestDTO) {
    Long id = friendshipService.request(
        friendshipRequestDTO.getRequesterId(), 
        friendshipRequestDTO.getReceiverId());
    return new ResponseEntity<>(id, HttpStatus.CREATED);
}

    /**
     * 받은 친구 요청을 수락합니다.
     * @param friendshipRequestDTO 요청자(requesterId)와 수신자(receiverId)의 ID를 담은 DTO.
     * @return 상태가 변경된 친구 관계 데이터의 ID를 반환합니다.
     */
    @PutMapping // HTTP PUT /friendships
    public ResponseEntity<Long> accept(@RequestBody FriendshipRequestDTO friendshipRequestDTO) {
        return ResponseEntity.ok(friendshipService.accept(
            friendshipRequestDTO.getRequesterId(), 
            friendshipRequestDTO.getReceiverId()));
    }

    /**
     * 친구 관계를 삭제(차단 또는 관계 끊기)합니다.
     * @param requesterId 요청을 보냈던 사용자의 ID.
     * @param receiverId 요청을 받았던 사용자의 ID.
     * @return 상태가 변경된 친구 관계 데이터의 ID를 반환합니다.
     */
    @DeleteMapping("/{requesterId}/{receiverId}") // HTTP DELETE /friendships/{requesterId}/{
    public ResponseEntity<Long> delete(@PathVariable Long requesterId,
                                        @PathVariable Long receiverId) {
        return ResponseEntity.ok(friendshipService.delete(requesterId, receiverId));
    }

    /**
     * 특정 사용자의 수락된(ACCEPTED) 친구 목록을 조회합니다.
     * @param userId 친구 목록을 조회할 사용자의 ID.
     * @return 친구들의 정보를 담은 DTO 리스트를 반환합니다.
     */
    @GetMapping("/accepted/{userId}") // HTTP GET /friendships/accepted/{userId}
    public ResponseEntity<List<FriendshipResponseDTO>> findFriendsAccepted(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.findFriendsAccepted(userId));
    }

    /**
     * 특정 사용자가 받은 친구 요청 목록을 조회합니다. (상태: REQUESTING)
     * @param userId 친구 요청 목록을 조회할 사용자의 ID.
     * @return 요청을 보낸 사용자들의 정보를 담은 DTO 리스트를 반환합니다.
     */
    @GetMapping("/received-requests/{userId}") // HTTP GET /friendships/received-requests/{userId}
    public ResponseEntity<List<FriendshipResponseDTO>> findReceivedRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.findReceivedRequests(userId));
    }

    /**
     * 특정 사용자가 보낸 친구 요청 목록을 조회합니다. (상태: REQUESTING)
     * @param userId 보낸 친구 요청 목록을 조회할 사용자의 ID.
     * @return 요청을 받은 사용자들의 정보를 담은 DTO 리스트를 반환합니다.
     */
    @GetMapping("/sent-requests/{userId}") // HTTP GET /friendships/sent-requests/{userId}
    public ResponseEntity<List<FriendshipResponseDTO>> findSentRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.findSentRequests(userId));
    }

}
