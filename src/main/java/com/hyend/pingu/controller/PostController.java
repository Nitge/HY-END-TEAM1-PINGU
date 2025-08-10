package com.hyend.pingu.controller;

import com.hyend.pingu.dto.PostRequestDTO;
import com.hyend.pingu.dto.PostResponseDTO;
import com.hyend.pingu.service.FileService;
import com.hyend.pingu.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class PostController {

    private final PostService postService;


    // TODO: userId로 조회 필요
    @GetMapping("/post")
    public ResponseEntity<Page<PostResponseDTO>> getPosts(
            @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.getPosts(pageable));
    }


    @PostMapping("/post")
    public ResponseEntity<Long> registerPost(@ModelAttribute PostRequestDTO postRequestDTO) throws IOException {

        Long registeredPostId = postService.register(postRequestDTO);

        return ResponseEntity.ok(registeredPostId);
    }

    @PutMapping("/post")
    public ResponseEntity<Long> updatePost(@ModelAttribute PostRequestDTO postRequestDTO) throws IOException {

        Long updatedPostId = postService.modify(postRequestDTO);

        return ResponseEntity.ok(updatedPostId);
    }

    @DeleteMapping("/post{postId}")
    public ResponseEntity<Long> deletePost(@PathVariable Long postId) {

        Long deletedPostId = postService.delete(postId);

        return ResponseEntity.ok(deletedPostId);
    }
}
