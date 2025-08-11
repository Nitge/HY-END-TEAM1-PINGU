package com.hyend.pingu.service;

import com.hyend.pingu.dto.PageRequestDTO;
import com.hyend.pingu.dto.PageResultDTO;
import com.hyend.pingu.dto.PostRequestDTO;
import com.hyend.pingu.dto.PostResponseDTO;
import com.hyend.pingu.entity.PostEntity;

import java.io.IOException;

public interface PostService {

    PageResultDTO<PostResponseDTO, PostEntity> getPosts(Long userId, PageRequestDTO pageRequestDTO);

    Long register(PostRequestDTO postRequestDTO) throws IOException;

    Long modify(PostRequestDTO postRequestDTO) throws IOException;

    Long delete(Long postId);
}
