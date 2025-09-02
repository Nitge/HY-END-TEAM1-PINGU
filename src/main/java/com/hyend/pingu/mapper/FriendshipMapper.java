package com.hyend.pingu.mapper;

import org.springframework.stereotype.Component;

import com.hyend.pingu.dto.FriendshipResponseDTO;
import com.hyend.pingu.entity.Friendship;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FriendshipMapper {

    private final UserMapper userMapper;

    public FriendshipResponseDTO entityToDto(Friendship friendship) {
        return FriendshipResponseDTO.builder()
                .id(friendship.getId())
                .friend1(userMapper.entityToDto(friendship.getFriend1()))
                .friend2(userMapper.entityToDto(friendship.getFriend2()))
                .status(friendship.getStatus())
                .createdAt(friendship.getCreatedAt())
                .updatedAt(friendship.getUpdatedAt())
                .build();
    }

    public Friendship dtoToEntity(FriendshipResponseDTO friendshipDTO) {
        return Friendship.builder()
                .id(friendshipDTO.getId())
                .friend1(userMapper.dtoToEntity(friendshipDTO.getFriend1()))
                .friend2(userMapper.dtoToEntity(friendshipDTO.getFriend2()))
                .status(friendshipDTO.getStatus())
                .build();
    }
}
