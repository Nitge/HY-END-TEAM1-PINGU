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

@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final FriendshipMapper friendshipMapper;

    @Override
    public Long request(Long requesterId, Long receiverId) {

        if (requesterId.equals(receiverId)) {
            throw new RuntimeException("자기 자신에게는 친구 추가 요청을 할 수 없습니다.");
        }
        if (friendshipRepository.findFriendshipWithStatus(requesterId, receiverId, Status.REQUESTING).isPresent()) {
            throw new RuntimeException("이미 친구 추가 요청을 보냈습니다.");
        }
        if (friendshipRepository.findFriendshipWithStatus(requesterId, receiverId, Status.ACCEPTED).isPresent()) {
                throw new RuntimeException("이미 친구 상태입니다.");
        }
        if (friendshipRepository.findFriendshipWithStatus(receiverId, requesterId, Status.REQUESTING).isPresent()) {
            throw new RuntimeException("상대가 먼저 친구 요청을 보냈습니다.");
        }


        var pair = friendshipRepository.findFriendship(requesterId, receiverId);
        if (pair.isPresent()) {
            var f = pair.get();
            if (f.getStatus() == Status.DELETED) {
                f.changeStatus(Status.REQUESTING);
                friendshipRepository.save(f);
                return f.getId();
            }
        }

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 Entity가 없습니다."));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 Entity가 없습니다."));

        Friendship friendship = Friendship.builder()
                .friend1(requester)
                .friend2(receiver)
                .status(Status.REQUESTING)
                .build();

        friendshipRepository.save(friendship);
        return friendship.getId();
        
    }

    @Override
    public Long accept(Long requesterId, Long receiverId) {
        Friendship friendship = friendshipRepository.findFriendshipWithStatus(requesterId, receiverId, Status.REQUESTING)
                        .orElseThrow(() -> new RuntimeException("친구 추가 요청이 없습니다"));
        friendship.changeStatus(Status.ACCEPTED);
        friendshipRepository.save(friendship);
        return friendship.getId();
    }

    @Override
    public Long delete(Long requesterId, Long receiverId) {
        Friendship friendship = friendshipRepository.findFriendshipWithStatus(requesterId, receiverId, Status.ACCEPTED)
                .orElseThrow(() -> new RuntimeException("친구 상태가 아닙니다."));
        friendship.changeStatus(Status.DELETED);
        friendshipRepository.save(friendship);
        return friendship.getId();
    }

    @Override
    public List<FriendshipResponseDTO> findFriendsAccepted(Long userId) {
        return friendshipRepository.findFriendsAccepted(userId)
                .stream()
                .map(friendshipMapper::entityToDto)
                .toList();
    }

    @Override
    public List<FriendshipResponseDTO> findReceivedRequests(Long userId) {
        return friendshipRepository.findReceivedRequests(userId)
                .stream()
                .map(friendshipMapper::entityToDto)
                .toList();
    }

    @Override
    public List<FriendshipResponseDTO> findSentRequests(Long userId) {
        return friendshipRepository.findSentRequests(userId)
                .stream()
                .map(friendshipMapper::entityToDto)
                .toList();
    }
}
