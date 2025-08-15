package com.hyend.pingu.service;

import com.hyend.pingu.dto.PageRequestDTO;
import com.hyend.pingu.dto.PageResultDTO;
import com.hyend.pingu.dto.user.UserDTO;
import com.hyend.pingu.entity.User;
import com.hyend.pingu.mapper.UserMapper;
import com.hyend.pingu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Long create(UserDTO userDTO) {
        User user = userMapper.dtoToEntity(userDTO);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public UserDTO read(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        return userMapper.entityToDto(user);
    }

    @Override
    public void update(UserDTO userDTO) {
        User user = userMapper.dtoToEntity(userDTO);
        userRepository.save(user);
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public PageResultDTO<UserDTO, User> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("userId").descending());
        Page<User> result = userRepository.findAll(pageable);
        Function<User, UserDTO> fn = userMapper::entityToDto;
        return new PageResultDTO<>(result, fn);
    }
}
