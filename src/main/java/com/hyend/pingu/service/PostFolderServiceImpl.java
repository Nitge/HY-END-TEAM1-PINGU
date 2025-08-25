package com.hyend.pingu.service;

import com.hyend.pingu.dto.PostFolderDTO;
import com.hyend.pingu.entity.PostFolder;
import com.hyend.pingu.entity.User;
import com.hyend.pingu.mapper.PostFolderMapper;
import com.hyend.pingu.repository.PostFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostFolderServiceImpl implements PostFolderService {

    private final PostFolderRepository postFolderRepository;
    private final PostFolderMapper postFolderMapper;


    @Override
    public List<PostFolderDTO> getPostFoldersByUser(Long userId) {

        User user = User.builder()
                .id(userId)
                .build();

        List<PostFolder> folders = postFolderRepository.findByUser(user);

        List<PostFolderDTO> postFolderDTOs = folders.stream()
                .map(postFolderMapper::entityToDto).toList();

        return postFolderDTOs;
    }

    @Override
    public Long createFolder(PostFolderDTO postFolderDTO) {

        User user = User.builder()
                .id(postFolderDTO.getUserId())
                .build();

        PostFolder postFolder = PostFolder.builder()
                .user(user)
                .name(postFolderDTO.getName())
                .build();

        postFolderRepository.save(postFolder);

        return postFolder.getId();
    }

    @Override
    public Long modifyFolder(PostFolderDTO postFolderDTO) {
        PostFolder postFolder = postFolderRepository.findById(postFolderDTO.getId())
                .orElseThrow(() -> new RuntimeException("폴더가 없음"));

        postFolder.changeName(postFolderDTO.getName());
        postFolder.changePosts(postFolderDTO);

        postFolderRepository.save(postFolder);

        return postFolder.getId();
    }

    @Override
    public Long deleteFolder(Long folderId) {
        postFolderRepository.deleteById(folderId);
        return folderId;
    }
}
