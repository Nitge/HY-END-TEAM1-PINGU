package com.hyend.pingu.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PostFolderDTO {

    private Long id;

    private String name;

    private Long userId;

    private List<Long> postIds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
