package com.hyend.pingu.controller;

import com.hyend.pingu.dto.PostResponseDTO;
import com.hyend.pingu.entity.PostEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

import com.hyend.pingu.dto.PageRequestDTO;
import com.hyend.pingu.dto.PageResultDTO;
import com.hyend.pingu.dto.user.UserDTO;
import com.hyend.pingu.entity.UserEntity;
import com.hyend.pingu.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageResultDTO<UserDTO, UserEntity>> getList(PageRequestDTO pageRequestDTO) {
        PageResultDTO<UserDTO, UserEntity> result = userService.getList(pageRequestDTO);
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
