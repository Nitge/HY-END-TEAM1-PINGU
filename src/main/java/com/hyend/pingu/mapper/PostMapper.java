package com.hyend.pingu.mapper;

import com.hyend.pingu.dto.FileResponseDTO;
import com.hyend.pingu.dto.PostRequestDTO;
import com.hyend.pingu.dto.PostResponseDTO;
import com.hyend.pingu.entity.PostEntity;
import com.hyend.pingu.entity.UserEntity;
import com.hyend.pingu.enumeration.Scope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final FileMapper fileMapper;

    public PostEntity dtoToEntity(PostRequestDTO postRequestDTO) {

        UserEntity userEntity = UserEntity.builder()
                .userId(postRequestDTO.getUserId())
                .build();

        PostEntity postEntity = PostEntity.builder()
                .user(userEntity)
                .title(postRequestDTO.getTitle())
                .content(postRequestDTO.getContent())
                .likeCount(0)
                .viewCount(0)
                .latitude(postRequestDTO.getLatitude())
                .longitude(postRequestDTO.getLongitude())
                .scope(Scope.valueOf(postRequestDTO.getScope()))
                .build();

        return postEntity;
    }

    public PostResponseDTO entityToDto(PostEntity postEntity) {

        List<FileResponseDTO> fileResponseDTOList = postEntity
                .getFiles()
                .stream()
                .map(fileEntity -> fileMapper.EntityToDto(fileEntity))
                .toList();

        return PostResponseDTO.builder()
                .postId(postEntity.getPostId())
                .userId(postEntity.getUser().getUserId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .likeCount(postEntity.getLikeCount())
                .viewCount(postEntity.getViewCount())
                .latitude(postEntity.getLatitude())
                .longitude(postEntity.getLongitude())
                .scope(postEntity.getScope().toString())
                .files(fileResponseDTOList)
                .build();
    }
}
