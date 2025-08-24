package com.hyend.pingu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipRequestDTO {
    
    private Long requesterId;

    private Long receiverId;
}
