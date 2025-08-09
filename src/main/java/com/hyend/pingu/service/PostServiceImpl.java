package com.hyend.pingu.service;

import com.hyend.pingu.dto.PostRequestDTO;
import com.hyend.pingu.dto.PostResponseDTO;
import com.hyend.pingu.entity.FileEntity;
import com.hyend.pingu.entity.FileInfo;
import com.hyend.pingu.entity.PostEntity;
import com.hyend.pingu.enumeration.Scope;
import com.hyend.pingu.mapper.PostMapper;
import com.hyend.pingu.repository.PostRepository;
import com.hyend.pingu.util.FileStoreUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final FileStoreUtil fileStoreUtil;

    @Override
    public Page<PostResponseDTO> getPosts(Pageable pageable) {
        return postRepository
                .findAll(pageable)
                .map(postEntity -> postMapper.entityToDto(postEntity));
    }

    @Override
    public Long register(PostRequestDTO postRequestDTO) throws IOException {

        PostEntity postEntity = postMapper.dtoToEntity(postRequestDTO);

        return setFilesAndSave(postRequestDTO, postEntity);
    }

    @Override
    public Long modify(PostRequestDTO postRequestDTO) throws IOException {

        PostEntity postEntity = postRepository.findById(postRequestDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 Entity가 없습니다."));

        postEntity.changeTitle(postRequestDTO.getTitle());
        postEntity.changeContent(postRequestDTO.getContent());
        postEntity.changeScope(Scope.valueOf(postRequestDTO.getScope()));

        return setFilesAndSave(postRequestDTO, postEntity);
    }

    @Override
    public Long delete(Long postId) {

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 Entity가 없습니다."));

        postEntity.changeScope(Scope.DELETED);

        return postId;
    }

    private Long setFilesAndSave(PostRequestDTO postRequestDTO, PostEntity postEntity) throws IOException {

        List<FileInfo> fileInfos = fileStoreUtil.storeFiles(postRequestDTO.getFiles());

        List<FileEntity> fileEntities = fileInfos.stream().map(
                        fileInfo -> FileEntity.builder()
                                .post(postEntity)
                                .fileInfo(fileInfo)
                                .build()
                )
                .toList();

        postEntity.changeFiles(fileEntities);

        postRepository.save(postEntity);

        return postEntity.getPostId();
    }
}
