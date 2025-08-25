package com.hyend.pingu.service;

import com.hyend.pingu.dto.PageRequestDTO;
import com.hyend.pingu.dto.PageResultDTO;
import com.hyend.pingu.dto.PostRequestDTO;
import com.hyend.pingu.dto.PostResponseDTO;
import com.hyend.pingu.entity.File;
import com.hyend.pingu.entity.FileInfo;
import com.hyend.pingu.entity.Post;
import com.hyend.pingu.enumeration.Scope;
import com.hyend.pingu.mapper.PostMapper;
import com.hyend.pingu.repository.PostRepository;
import com.hyend.pingu.util.FileStoreUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final FileStoreUtil fileStoreUtil;
    private final GeometryFactory geometryFactory;

    @Override
    public PageResultDTO<PostResponseDTO, Post> getPosts(Long userId, PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("createdAt").descending());
        Page<Post> postEntities;

        if(userId != null) {
            postEntities = postRepository.findAllByUserId(userId, pageable);
        } else {
            postEntities = postRepository.findAll(pageable);
        }

        return new PageResultDTO<>(postEntities, postMapper::entityToDto);
    }

    @Override
    public List<PostResponseDTO> getNearPosts(Double longitude, Double latitude, double distance) {

        Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        List<Post> nearPosts = postRepository.findNear(location, distance);

        return nearPosts.stream()
                .map(postMapper::entityToDto)
                .toList();
    }

    @Override
    public Long register(PostRequestDTO postRequestDTO) throws IOException {

        Post post = postMapper.dtoToEntity(postRequestDTO);

        return setFilesAndSave(postRequestDTO, post);
    }

    @Override
    public Long modify(Long postId, PostRequestDTO postRequestDTO) throws IOException {
    
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 Entity가 없습니다."));
    
        if (postRequestDTO.getTitle() != null) {
            post.changeTitle(postRequestDTO.getTitle());
        }
        if (postRequestDTO.getContent() != null) {
            post.changeContent(postRequestDTO.getContent());
        }
        if (postRequestDTO.getScope() != null && !postRequestDTO.getScope().isBlank()) {
            post.changeScope(Scope.valueOf(postRequestDTO.getScope()));
        }
        if (postRequestDTO.getLongitude() != null && postRequestDTO.getLatitude() != null ) {
            Point location = geometryFactory.createPoint(
                    new Coordinate(postRequestDTO.getLongitude(), postRequestDTO.getLatitude())
            );
            post.changeLocation(location);
        }
        
        return setFilesAndSave(postRequestDTO, post);
    }

    @Override
    public Long delete(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 Entity가 없습니다."));

        post.changeScope(Scope.DELETED);
        postRepository.save(post);

        return postId;
    }

    @Override
    public PostResponseDTO getPost(Long postId, boolean count) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 Entity가 없습니다."));
        
        if(count) {
            post.increaseViewCount();
            postRepository.save(post);
        }

        return postMapper.entityToDto(post);

    }

    private Long setFilesAndSave(PostRequestDTO postRequestDTO, Post post) throws IOException {

        if (postRequestDTO.getFiles() != null && !postRequestDTO.getFiles().isEmpty()) {
            List<FileInfo> fileInfos = fileStoreUtil.storeFiles(postRequestDTO.getFiles());
    
            List<File> fileEntities = fileInfos.stream()
                    .map(
                            fileInfo -> File.builder()
                                    .post(post)
                                    .fileInfo(fileInfo)
                                    .build()
                    )
                    .toList();
    
            post.changeFiles(fileEntities);
        }
    
        postRepository.save(post);
        return post.getId();
    }
}
