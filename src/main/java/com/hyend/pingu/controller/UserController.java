package com.hyend.pingu.controller;

import com.hyend.pingu.dto.PageRequestDTO;
import com.hyend.pingu.dto.PageResultDTO;
import com.hyend.pingu.dto.user.UserDTO;
import com.hyend.pingu.entity.User;
import com.hyend.pingu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController.java
 * 사용자(User) 정보와 관련된 API 요청을 처리하는 컨트롤러입니다.
 * 사용자 생성(회원가입), 조회, 수정, 삭제 등의 기능을 제공합니다.
 */
@RestController // 이 클래스가 RESTful API의 컨트롤러임을 나타냅니다.
@RequestMapping("/users") // 이 컨트롤러의 모든 API는 '/users' 경로로 시작됩니다.
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 전체 사용자 목록을 페이징하여 조회합니다.
     * @param pageRequestDTO 페이지 번호, 페이지 당 개수 등의 페이징 정보를 담은 DTO.
     * @return 페이징 처리된 사용자 목록과 페이지 정보를 담은 DTO를 반환합니다.
     */
    @GetMapping // HTTP GET /users
    public ResponseEntity<PageResultDTO<UserDTO, User>> getList(PageRequestDTO pageRequestDTO) {
        // 서비스 계층을 호출하여 페이징된 사용자 목록을 가져옵니다.
        PageResultDTO<UserDTO, User> result = userService.getList(pageRequestDTO);
        // HTTP 200 OK 상태 코드와 함께 결과를 응답합니다.
        return ResponseEntity.ok(result);
    }

    /**
     * 새로운 사용자를 생성합니다. (회원가입)
     * @param userDTO 생성할 사용자의 정보(이름, 이메일 등)를 담은 DTO.
     * @return 생성된 사용자의 고유 ID를 반환합니다.
     */
    @PostMapping // HTTP POST /users
    public ResponseEntity<Long> createUser(@RequestBody UserDTO userDTO) {
        // 서비스 계층을 호출하여 사용자를 생성하고, 생성된 ID를 받습니다.
        Long id = userService.create(userDTO);
        // HTTP 201 Created 상태 코드와 함께 생성된 ID를 응답합니다.
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    /**
     * 특정 사용자 한 명의 정보를 조회합니다.
     * @param userId 조회할 사용자의 ID.
     * @return 조회된 사용자의 정보를 담은 DTO를 반환합니다.
     */
    @GetMapping("/{userId}") // HTTP GET /users/{userId}
    public ResponseEntity<UserDTO> readUser(@PathVariable Long userId) {
        // 서비스 계층을 호출하여 특정 사용자 정보를 조회합니다.
        UserDTO dto = userService.read(userId);
        // HTTP 200 OK 상태 코드와 함께 사용자 정보를 응답합니다.
        return ResponseEntity.ok(dto);
    }

    /**
     * 특정 사용자의 정보를 수정합니다.
     * @param userId 수정할 사용자의 ID.
     * @param userDTO 수정할 내용을 담은 DTO.
     * @return 내용 없이 HTTP 204 No Content 상태 코드를 반환하여 성공을 알립니다.
     */
    @PutMapping("/{userId}") // HTTP PUT /users/{userId}
    public ResponseEntity<Void> updateUser(@PathVariable Long userId,
                                           @RequestBody UserDTO userDTO) {
        // DTO에 PathVariable로 받은 userId를 설정해줍니다.
        // 이는 어떤 사용자를 수정할지 명확히 하기 위함입니다.
        userDTO.setUserId(userId);
        // 서비스 계층을 호출하여 사용자 정보를 수정합니다.
        userService.update(userDTO);
        // 별도의 데이터를 반환할 필요가 없으므로 noContent()를 사용합니다.
        return ResponseEntity.noContent().build();
    }

    /**
     * 특정 사용자를 삭제합니다.
     * @param userId 삭제할 사용자의 ID.
     * @return 내용 없이 HTTP 204 No Content 상태 코드를 반환하여 성공을 알립니다.
     */
    @DeleteMapping("/{userId}") // HTTP DELETE /users/{userId}
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        // 서비스 계층을 호출하여 사용자를 삭제합니다.
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
