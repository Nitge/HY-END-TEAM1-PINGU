package com.hyend.pingu.dto;

import com.hyend.pingu.entity.PinEntity;
import com.hyend.pingu.entity.PostEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PinDetailResponseDTO {

    private Long pinId;
    private Long postId;
    private String title;
    private String content;
    private float latitude;
    private float longitude;
    private long likeCount;
    private long viewCount;
    private String username;
    private LocalDateTime createdAt;

    public PinDetailResponseDTO(PinEntity pinEntity){
        PostEntity post = pinEntity.getPost();

        this.pinId = pinEntity.getPinId();
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.likeCount = post.getLike();
        this.viewCount = post.getViewCount();
        this.username = pinEntity.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
    }
}
