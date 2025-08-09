package com.hyend.pingu.service;

import com.hyend.pingu.dto.PostRequestDTO;
import com.hyend.pingu.dto.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface PostService {

    public Page<PostResponseDTO> getPosts(Pageable pageable);

    public Long register(PostRequestDTO postRequestDTO) throws IOException;

    public Long modify(PostRequestDTO postRequestDTO) throws IOException;

    public Long delete(Long postId);

}
