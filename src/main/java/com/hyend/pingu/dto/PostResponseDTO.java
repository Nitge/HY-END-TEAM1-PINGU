package com.hyend.pingu.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostResponseDTO {

    private Long postId;

    private Long userId;

    private String title;

    private String content;

    private Long likeCount;

    private Long viewCount;

    private Double longitude;

    private Double latitude;

    private String scope;

    private List<FileResponseDTO> files;

}
