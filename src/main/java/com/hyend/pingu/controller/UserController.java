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

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageResultDTO<UserDTO, User>> getList(PageRequestDTO pageRequestDTO) {
        PageResultDTO<UserDTO, User> result = userService.getList(pageRequestDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody UserDTO userDTO) {
        Long id = userService.create(userDTO);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> readUser(@PathVariable Long userId) {
        UserDTO dto = userService.read(userId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId,
                                           @RequestBody UserDTO userDTO) {
        userDTO.setUserId(userId);
        userService.update(userDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
