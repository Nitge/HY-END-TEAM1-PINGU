package com.hyend.pingu.mapper;

import com.hyend.pingu.dto.FileResponseDTO;
import com.hyend.pingu.dto.PostRequestDTO;
import com.hyend.pingu.dto.PostResponseDTO;
import com.hyend.pingu.entity.Post;
import com.hyend.pingu.entity.User;
import com.hyend.pingu.enumeration.Scope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final FileMapper fileMapper;

    public Post dtoToEntity(PostRequestDTO postRequestDTO) {

        User user = User.builder()
                .id(postRequestDTO.getUserId())
                .build();

        Post post = Post.builder()
                .user(user)
                .title(postRequestDTO.getTitle())
                .content(postRequestDTO.getContent())
                .likeCount(0)
                .viewCount(0)
                .latitude(postRequestDTO.getLatitude())
                .longitude(postRequestDTO.getLongitude())
                .scope(Scope.valueOf(postRequestDTO.getScope()))
                .build();

        return post;
    }

    public PostResponseDTO entityToDto(Post post) {

        List<FileResponseDTO> fileResponseDTOList = post
                .getFiles()
                .stream()
                .map(fileEntity -> fileMapper.EntityToDto(fileEntity))
                .toList();

        return PostResponseDTO.builder()
                .postId(post.getId())
                .userId(post.getUser().getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .scope(post.getScope().toString())
                .files(fileResponseDTOList)
                .build();
    }
}
