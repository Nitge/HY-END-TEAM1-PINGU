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

    private Long userId;

    private String title;

    private String content;

    private Long likeCount;

    private Long viewCount;

    private Double longitude;

    private Double latitude;

    private String scope;

    private List<MultipartFile> files;
}
