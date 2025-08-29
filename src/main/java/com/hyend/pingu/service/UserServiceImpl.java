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

/**
 * UserServiceImpl.java
 * UserService 인터페이스의 실제 구현체입니다.
 * 사용자 데이터의 생성, 조회, 수정, 삭제(CRUD)와 관련된 핵심 비즈니스 로직을 담당합니다.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * DTO를 받아 새로운 사용자를 생성하고 DB에 저장합니다.
     * @param userDTO 생성할 사용자의 정보가 담긴 DTO.
     * @return 생성된 사용자의 고유 ID.
     */
    @Override
    public Long create(UserDTO userDTO) {
        // 1. Mapper를 사용하여 DTO를 User 엔티티로 변환합니다.
        User user = userMapper.dtoToEntity(userDTO);
        // 2. Repository를 통해 변환된 엔티티를 DB에 저장합니다.
        userRepository.save(user);
        // 3. 저장된 엔티티의 ID를 반환합니다.
        return user.getId();
    }

    /**
     * 사용자 ID로 특정 사용자를 조회합니다.
     * @param userId 조회할 사용자의 ID.
     * @return 조회된 사용자의 정보를 담은 DTO.
     */
    @Override
    public UserDTO read(Long userId) {
        // 1. Repository를 통해 ID로 User 엔티티를 조회합니다.
        //    만약 해당 ID의 사용자가 없으면 RuntimeException을 발생시킵니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        // 2. 조회된 엔티티를 Mapper를 사용하여 DTO로 변환한 후 반환합니다.
        return userMapper.entityToDto(user);
    }

    /**
     * DTO를 받아 기존 사용자의 정보를 수정합니다.
     * @param userDTO 수정할 정보가 담긴 DTO (반드시 userId를 포함해야 함).
     */
    @Override
    public void update(UserDTO userDTO) {
        // 1. Mapper를 사용하여 DTO를 User 엔티티로 변환합니다.
        User user = userMapper.dtoToEntity(userDTO);
        // 2. JpaRepository의 save() 메소드는 전달된 엔티티의 ID가 DB에 이미 존재하면
        //    새로 INSERT하는 대신 기존 데이터를 UPDATE합니다.
        userRepository.save(user);
    }

    /**
     * 사용자 ID로 특정 사용자를 DB에서 삭제합니다.
     * @param userId 삭제할 사용자의 ID.
     */
    @Override
    public void delete(Long userId) {
        // Repository를 통해 ID에 해당하는 사용자를 DB에서 삭제합니다.
        userRepository.deleteById(userId);
    }

    /**
     * 전체 사용자 목록을 페이징하여 조회합니다.
     * @param requestDTO 페이징 요청 정보(페이지 번호, 사이즈 등)가 담긴 DTO.
     * @return 페이징된 결과를 담은 PageResultDTO 객체.
     */
    @Override
    public PageResultDTO<UserDTO, User> getList(PageRequestDTO requestDTO) {
        // 1. PageRequestDTO를 사용하여 Pageable 객체를 생성합니다. 정렬 기준은 'userId'의 내림차순입니다.
        Pageable pageable = requestDTO.getPageable(Sort.by("userId").descending());
        // 2. Repository의 findAll 메소드에 Pageable 객체를 전달하여 페이징된 결과를 받습니다.
        Page<User> result = userRepository.findAll(pageable);
        // 3. Page<User>의 각 User 엔티티를 UserDTO로 변환할 함수(fn)를 정의합니다.
        Function<User, UserDTO> fn = userMapper::entityToDto;
        // 4. 최종적으로 페이징된 엔티티 결과와 변환 함수를 PageResultDTO 생성자에 넘겨 결과를 반환합니다.
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new RuntimeException("ID or password does not match"));
    }
}
