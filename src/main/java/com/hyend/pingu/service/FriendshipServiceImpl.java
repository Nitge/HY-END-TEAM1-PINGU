package com.hyend.pingu.service;

import com.hyend.pingu.dto.FriendshipResponseDTO;
import com.hyend.pingu.entity.Friendship;
import com.hyend.pingu.entity.User;
import com.hyend.pingu.enumeration.Status;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyend.pingu.mapper.FriendshipMapper;
import com.hyend.pingu.repository.FriendshipRepository;
import com.hyend.pingu.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * FriendshipServiceImpl.java
 * FriendshipService 인터페이스의 실제 구현체입니다.
 * 친구 요청, 수락, 삭제, 조회 등 친구 관계와 관련된 핵심 비즈니스 로직을 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional // 클래스 레벨에 @Transactional을 선언하면 모든 public 메소드가 트랜잭션 내에서 동작합니다.
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final FriendshipMapper friendshipMapper;

    /**
     * 친구 요청을 생성합니다. 다양한 예외 케이스를 검증한 후 요청을 저장합니다.
     * @param requesterId 요청을 보내는 사용자의 ID
     * @param receiverId 요청을 받는 사용자의 ID
     * @return 생성된 Friendship 엔티티의 ID
     */
    @Override
    public Long request(Long requesterId, Long receiverId) {
        // [예외 처리 1] 자기 자신에게 친구 요청을 보내는 경우
        if (requesterId.equals(receiverId)) {
            throw new RuntimeException("자기 자신에게는 친구 추가 요청을 할 수 없습니다.");
        }
        // [예외 처리 2] 이미 해당 사용자에게 친구 요청을 보낸 경우 (중복 요청 방지)
        if (friendshipRepository.findFriendshipWithStatus(requesterId, receiverId, Status.REQUESTING).isPresent()) {
            throw new RuntimeException("이미 친구 추가 요청을 보냈습니다.");
        }
        // [예외 처리 3] 이미 친구 관계인 경우
        if (friendshipRepository.findFriendshipWithStatus(requesterId, receiverId, Status.ACCEPTED).isPresent()) {
            throw new RuntimeException("이미 친구 상태입니다.");
        }
        // [예외 처리 4] 상대방이 나에게 먼저 친구 요청을 보낸 경우 (수락을 유도)
        if (friendshipRepository.findFriendshipWithStatus(receiverId, requesterId, Status.REQUESTING).isPresent()) {
            throw new RuntimeException("상대가 먼저 친구 요청을 보냈습니다.");
        }

        // 과거에 친구였다가 삭제(DELETED)한 경우, 새로운 관계를 만들지 않고 기존 관계의 상태를 변경합니다.
        var pair = friendshipRepository.findFriendship(requesterId, receiverId);
        if (pair.isPresent()) {
            var f = pair.get();
            if (f.getStatus() == Status.DELETED) {
                f.changeStatus(Status.REQUESTING);
                friendshipRepository.save(f);
                return f.getId();
            }
        }

        // DB에서 요청자와 수신자 User 엔티티를 조회합니다.
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 Entity가 없습니다."));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 Entity가 없습니다."));

        // 새로운 Friendship 엔티티를 생성합니다.
        Friendship friendship = Friendship.builder()
                .friend1(requester)
                .friend2(receiver)
                .status(Status.REQUESTING) // 초기 상태는 '요청 중'
                .build();

        // 생성된 엔티티를 DB에 저장하고 ID를 반환합니다.
        friendshipRepository.save(friendship);
        return friendship.getId();
    }

    /**
     * 친구 요청을 수락합니다.
     * @param requesterId 요청을 보냈던 사용자의 ID
     * @param receiverId 요청을 받은 사용자의 ID (현재 로그인한 사용자)
     * @return 상태가 변경된 Friendship 엔티티의 ID
     */
    @Override
    public Long accept(Long requesterId, Long receiverId) {
        // 1. 요청자와 수신자 ID로 'REQUESTING' 상태인 친구 관계를 조회합니다. 없으면 예외 발생.
        Friendship friendship = friendshipRepository.findFriendshipWithStatus(requesterId, receiverId, Status.REQUESTING)
                .orElseThrow(() -> new RuntimeException("친구 추가 요청이 없습니다"));
        // 2. 상태를 'ACCEPTED'로 변경합니다.
        friendship.changeStatus(Status.ACCEPTED);
        // 3. 변경된 상태를 DB에 저장합니다.
        friendshipRepository.save(friendship);
        return friendship.getId();
    }

    /**
     * 친구 관계를 삭제합니다. (상태를 DELETED로 변경)
     * @param requesterId 요청을 보냈던 사용자의 ID
     * @param receiverId 요청을 받았던 사용자의 ID
     * @return 상태가 변경된 Friendship 엔티티의 ID
     */
    @Override
    public Long delete(Long requesterId, Long receiverId) {
        // 1. 두 사용자 ID로 'ACCEPTED' 상태인 친구 관계를 조회합니다. 없으면 예외 발생.
        Friendship friendship = friendshipRepository.findFriendshipWithStatus(requesterId, receiverId, Status.ACCEPTED)
                .orElseThrow(() -> new RuntimeException("친구 상태가 아닙니다."));
        // 2. 상태를 'DELETED'로 변경합니다.
        friendship.changeStatus(Status.DELETED);
        // 3. 변경된 상태를 DB에 저장합니다.
        friendshipRepository.save(friendship);
        return friendship.getId();
    }

    /**
     * 특정 사용자의 친구 목록(상태: ACCEPTED)을 조회합니다.
     * @param userId 조회할 사용자의 ID
     * @return 친구 정보 DTO 리스트
     */
    @Override
    @Transactional(readOnly = true) // 데이터 변경이 없는 조회 작업이므로 readOnly=true로 성능 최적화
    public List<FriendshipResponseDTO> findFriendsAccepted(Long userId) {
        // 1. Repository를 통해 친구 관계 엔티티 목록을 조회합니다.
        List<Friendship> friendships = friendshipRepository.findFriendsAccepted(userId);
        // 2. Stream API와 Mapper를 사용하여 엔티티 리스트를 DTO 리스트로 변환하여 반환합니다.
        return friendships.stream()
                .map(friendshipMapper::entityToDto)
                .toList();
    }

    /**
     * 특정 사용자가 받은 친구 요청 목록(상태: REQUESTING)을 조회합니다.
     * @param userId 조회할 사용자의 ID
     * @return 친구 요청 정보 DTO 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<FriendshipResponseDTO> findReceivedRequests(Long userId) {
        return friendshipRepository.findReceivedRequests(userId)
                .stream()
                .map(friendshipMapper::entityToDto)
                .toList();
    }

    /**
     * 특정 사용자가 보낸 친구 요청 목록(상태: REQUESTING)을 조회합니다.
     * @param userId 조회할 사용자의 ID
     * @return 친구 요청 정보 DTO 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<FriendshipResponseDTO> findSentRequests(Long userId) {
        return friendshipRepository.findSentRequests(userId)
                .stream()
                .map(friendshipMapper::entityToDto)
                .toList();
    }
}
