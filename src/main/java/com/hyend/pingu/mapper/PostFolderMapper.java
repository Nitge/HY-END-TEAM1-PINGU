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
                .postIds(entity.getPosts().stream()
                        .map(Post::getId).toList())
                .build();

        return postFolderDTO;
    }

    /*
    public PostFolder DtoToEntity(PostFolderDTO dto) {
        PostFolder.builder()
                .id(dto.getId())
                .name(dto.getName())
                .user()
    }
     */

}
