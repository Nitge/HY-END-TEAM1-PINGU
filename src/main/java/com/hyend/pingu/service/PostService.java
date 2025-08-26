package com.hyend.pingu.service;

import com.hyend.pingu.dto.PageRequestDTO;
import com.hyend.pingu.dto.PageResultDTO;
import com.hyend.pingu.dto.PostRequestDTO;
import com.hyend.pingu.dto.PostResponseDTO;
import com.hyend.pingu.entity.Post;

import java.io.IOException;
import java.util.List;

public interface PostService {

    PageResultDTO<PostResponseDTO, Post> getPosts(Long userId, PageRequestDTO pageRequestDTO);

    List<PostResponseDTO> getNearPosts(Double longitude, Double latitude, double distance);

    Long register(PostRequestDTO postRequestDTO) throws IOException;

    Long modify(Long postId, PostRequestDTO postRequestDTO) throws IOException;

    Long delete(Long postId);

    default PostResponseDTO getPost(Long postId) {
        return getPost(postId, true);
    }
    
    PostResponseDTO getPost(Long postId, boolean count);

}
