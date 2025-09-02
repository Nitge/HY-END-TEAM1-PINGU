package com.hyend.pingu.dto;

import com.hyend.pingu.dto.user.UserDTO;
import com.hyend.pingu.enumeration.Status;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class FriendshipResponseDTO {

    private Long id;

    private UserDTO friend1;

    private UserDTO friend2;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

