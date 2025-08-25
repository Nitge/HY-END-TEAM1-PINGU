package com.hyend.pingu.service;

import com.hyend.pingu.dto.PostFolderDTO;

import java.util.List;

public interface PostFolderService {
    List<PostFolderDTO> getPostFoldersByUser(Long userId);

    Long createFolder(PostFolderDTO postFolderDTO);

    Long modifyFolder(PostFolderDTO postFolderDTO);

    Long deleteFolder(Long folderId);
}
