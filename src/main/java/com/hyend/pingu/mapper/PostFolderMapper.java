package com.hyend.pingu.mapper;

import com.hyend.pingu.dto.PostFolderDTO;
import com.hyend.pingu.entity.Post;
import com.hyend.pingu.entity.PostFolder;
import org.springframework.stereotype.Component;

@Component
public class PostFolderMapper {

    public PostFolderDTO entityToDto(PostFolder entity) {

        PostFolderDTO postFolderDTO = PostFolderDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .userId(entity.getUser().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .postIds(entity.getPosts().stream()
                        .map(Post::getId).toList())
                .build();

        return postFolderDTO;
    }
}
