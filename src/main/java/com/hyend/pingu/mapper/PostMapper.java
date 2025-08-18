package com.hyend.pingu.mapper;

import com.hyend.pingu.dto.FileResponseDTO;
import com.hyend.pingu.dto.PostRequestDTO;
import com.hyend.pingu.dto.PostResponseDTO;
import com.hyend.pingu.entity.Post;
import com.hyend.pingu.entity.User;
import com.hyend.pingu.enumeration.Scope;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final FileMapper fileMapper;
    private final GeometryFactory geometryFactory;

    public Post dtoToEntity(PostRequestDTO postRequestDTO) {

        User user = User.builder()
                .id(postRequestDTO.getUserId())
                .build();

        double lon = postRequestDTO.getLongitude() == null ? 0 : postRequestDTO.getLongitude();
        double lat = postRequestDTO.getLatitude() == null ? 0 : postRequestDTO.getLatitude();
        Scope scope = postRequestDTO.getScope() == null ? Scope.PUBLIC : Scope.valueOf(postRequestDTO.getScope());


        return Post.builder()
                .user(user)
                .title(postRequestDTO.getTitle())
                .content(postRequestDTO.getContent())
                .likeCount(0L)
                .viewCount(0L)
                .location(geometryFactory.createPoint(new Coordinate(lon, lat)))
                .scope(scope)
                .build();
    }

    public PostResponseDTO entityToDto(Post post) {

        List<FileResponseDTO> fileResponseDTOList = post
                .getFiles()
                .stream()
                .map(fileMapper::EntityToDto)
                .toList();

        return PostResponseDTO.builder()
                .postId(post.getId())
                .userId(post.getUser().getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .longitude(post.getLocation().getX())
                .latitude(post.getLocation().getY())
                .scope(post.getScope().toString())
                .files(fileResponseDTOList)
                .build();
    }
}
