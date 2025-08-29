package com.hyend.pingu.service;

import com.hyend.pingu.dto.PageRequestDTO;
import com.hyend.pingu.dto.PageResultDTO;
import com.hyend.pingu.dto.user.UserDTO;
import com.hyend.pingu.entity.User;

public interface UserService {
    Long create(UserDTO userDTO);
    UserDTO read(Long userId);
    void update(UserDTO userDTO);
    void delete(Long userId);

    PageResultDTO<UserDTO, User> getList(PageRequestDTO requestDTO);

    User login(String username, String password);
}
