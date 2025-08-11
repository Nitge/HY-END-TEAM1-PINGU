package com.hyend.pingu.service;

import com.hyend.pingu.dto.PageRequestDTO;
import com.hyend.pingu.dto.PageResultDTO;
import com.hyend.pingu.dto.user.UserDTO;
import com.hyend.pingu.entity.UserEntity;
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
        UserEntity userEntity = userMapper.dtoToEntity(userDTO);
        userRepository.save(userEntity);
        return userEntity.getUserId();
    }

    @Override
    public UserDTO read(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        return userMapper.entityToDto(userEntity);
    }

    @Override
    public void update(UserDTO userDTO) {
        UserEntity userEntity = userMapper.dtoToEntity(userDTO);
        userRepository.save(userEntity);
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public PageResultDTO<UserDTO, UserEntity> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("userId").descending());
        Page<UserEntity> result = userRepository.findAll(pageable);
        Function<UserEntity, UserDTO> fn = userMapper::entityToDto;
        return new PageResultDTO<>(result, fn);
    }
}
