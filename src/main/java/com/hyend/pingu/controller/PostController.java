package com.hyend.pingu.controller;

import com.hyend.pingu.dto.PageRequestDTO;
import com.hyend.pingu.dto.PageResultDTO;
import com.hyend.pingu.dto.PostRequestDTO;
import com.hyend.pingu.dto.PostResponseDTO;
import com.hyend.pingu.entity.PostEntity;
import com.hyend.pingu.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<PageResultDTO<PostResponseDTO, PostEntity>> getPosts(
            @RequestParam(required = false) Long userId,
            PageRequestDTO pageRequestDTO) {

        return ResponseEntity.ok(postService.getPosts(userId, pageRequestDTO));
    }

    @PostMapping
    public ResponseEntity<Long> registerPost(PostRequestDTO postRequestDTO) throws IOException {

        Long registeredPostId = postService.register(postRequestDTO);

        return ResponseEntity.ok(registeredPostId);
    }

    @PutMapping
    public ResponseEntity<Long> updatePost(PostRequestDTO postRequestDTO) throws IOException {

        Long updatedPostId = postService.modify(postRequestDTO);

        return ResponseEntity.ok(updatedPostId);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Long> deletePost(@PathVariable Long postId) {

        Long deletedPostId = postService.delete(postId);

        return ResponseEntity.ok(deletedPostId);
    }
}
