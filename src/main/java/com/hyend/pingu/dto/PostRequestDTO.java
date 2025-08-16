package com.hyend.pingu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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
