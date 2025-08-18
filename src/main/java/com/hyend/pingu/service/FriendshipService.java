package com.hyend.pingu.service;

import java.util.List;

import com.hyend.pingu.dto.FriendshipDTO;


public interface FriendshipService {

    Long request(Long requesterId, Long receiverId);

    Long accept(Long requesterId, Long receiverId);

    Long delete(Long requesterId, Long receiverId);

    List<FriendshipDTO> findFriendsAccepted(Long userId);

    List<FriendshipDTO> findReceivedRequests(Long userId);

    List<FriendshipDTO> findSentRequests(Long userId);

}
