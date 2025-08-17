package com.hyend.pingu.dto;

import com.hyend.pingu.dto.user.UserDTO;
import com.hyend.pingu.enumeration.Status;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FriendshipDTO {

    private Long id;

    private UserDTO friend1;

    private UserDTO friend2;

    private Status status;
}
