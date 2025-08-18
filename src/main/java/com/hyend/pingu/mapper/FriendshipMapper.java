package com.hyend.pingu.mapper;

import org.springframework.stereotype.Component;

import com.hyend.pingu.dto.FriendshipDTO;
import com.hyend.pingu.entity.Friendship;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FriendshipMapper {

    private final UserMapper userMapper;

    public FriendshipDTO entityToDto(Friendship friendship) {
        return FriendshipDTO.builder()
                .id(friendship.getId())
                .friend1(userMapper.entityToDto(friendship.getFriend1()))
                .friend2(userMapper.entityToDto(friendship.getFriend2()))
                .status(friendship.getStatus())
                .build();
    }

    public Friendship dtoToEntity(FriendshipDTO friendshipDTO) {
        return Friendship.builder()
                .id(friendshipDTO.getId())
                .friend1(userMapper.dtoToEntity(friendshipDTO.getFriend1()))
                .friend2(userMapper.dtoToEntity(friendshipDTO.getFriend2()))
                .status(friendshipDTO.getStatus())
                .build();
    }
}
