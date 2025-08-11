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
}
