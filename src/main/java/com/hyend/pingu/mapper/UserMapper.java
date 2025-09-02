package com.hyend.pingu.mapper;

import com.hyend.pingu.dto.user.UserDTO;
import com.hyend.pingu.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User dtoToEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getUserId())
                .name(dto.getUsername())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    public UserDTO entityToDto(User entity) {
        return UserDTO.builder()
                .userId(entity.getId())
                .username(entity.getName())
                .password(entity.getPassword())
                .phoneNumber(entity.getPhoneNumber())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
