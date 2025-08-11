package com.hyend.pingu.service;

import com.hyend.pingu.dto.PageRequestDTO;
import com.hyend.pingu.dto.PageResultDTO;
import com.hyend.pingu.dto.user.UserDTO;
import com.hyend.pingu.entity.UserEntity;

public interface UserService {
    Long create(UserDTO userDTO);
    UserDTO read(Long userId);
    void update(UserDTO userDTO);
    void delete(Long userId);

    PageResultDTO<UserDTO, UserEntity> getList(PageRequestDTO requestDTO);

    default UserEntity dtoToEntity(UserDTO dto) {
        return UserEntity.builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    default UserDTO entityToDto(UserEntity entity) {
        return UserDTO.builder()
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .phoneNumber(entity.getPhoneNumber())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }
}
