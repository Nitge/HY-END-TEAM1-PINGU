package com.hyend.pingu.service;

import java.util.List;

import com.hyend.pingu.dto.FriendshipResponseDTO;


public interface FriendshipService {

    Long request(Long requesterId, Long receiverId);

    Long accept(Long requesterId, Long receiverId);

    Long delete(Long requesterId, Long receiverId);

    List<FriendshipResponseDTO> findFriendsAccepted(Long userId);

    List<FriendshipResponseDTO> findReceivedRequests(Long userId);

    List<FriendshipResponseDTO> findSentRequests(Long userId);

}
