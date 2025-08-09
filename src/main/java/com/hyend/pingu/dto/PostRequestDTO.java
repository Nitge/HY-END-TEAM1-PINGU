package com.hyend.pingu.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
public class PostRequestDTO {

    private Long postId;

    private Long userId;

    private String title;

    private String content;

    private Long likeCount;

    private Long viewCount;

    private Float latitude;

    private Float longitude;

    private String scope;

    private List<MultipartFile> files;
}
