package com.hyend.pingu.mapper;

import com.hyend.pingu.dto.user.UserDTO;
import com.hyend.pingu.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity dtoToEntity(UserDTO dto) {
        return UserEntity.builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    public UserDTO entityToDto(UserEntity entity) {
        return UserDTO.builder()
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .phoneNumber(entity.getPhoneNumber())
                .regDate(entity.getCreatedAt())
                .modDate(entity.getUpdatedAt())
                .build();
    }
}
