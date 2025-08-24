package com.hyend.pingu.controller;

import com.hyend.pingu.dto.PageRequestDTO;
import com.hyend.pingu.dto.PageResultDTO;
import com.hyend.pingu.dto.PostRequestDTO;
import com.hyend.pingu.dto.PostResponseDTO;
import com.hyend.pingu.entity.Post;
import com.hyend.pingu.service.PostService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<PageResultDTO<PostResponseDTO, Post>> getPosts(
            @RequestParam(required = false) Long userId,
            @ModelAttribute PageRequestDTO pageRequestDTO) {

        return ResponseEntity.ok(postService.getPosts(userId, pageRequestDTO));
    }

    @GetMapping("/near")
    public ResponseEntity<List<PostResponseDTO>> getNearPosts(
            @RequestParam Double longitude,
            @RequestParam Double latitude,
            @RequestParam Double distance) {

        List<PostResponseDTO> nearPosts = postService.getNearPosts(longitude, latitude, distance);

        return ResponseEntity.ok(nearPosts);
    }

    @PostMapping
    public ResponseEntity<Long> registerPost(@ModelAttribute PostRequestDTO postRequestDTO) throws IOException {
    
        Long registeredPostId = postService.register(postRequestDTO);
    
        return new ResponseEntity<>(registeredPostId, HttpStatus.CREATED);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Long> updatePost(@PathVariable Long postId,
                                           @ModelAttribute PostRequestDTO postRequestDTO) throws IOException {

        Long updatedPostId = postService.modify(postId, postRequestDTO);
        return ResponseEntity.ok(updatedPostId);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Long> deletePost(@PathVariable Long postId) {

        Long deletedPostId = postService.delete(postId);

        return ResponseEntity.ok(deletedPostId);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long postId,
                                                   @RequestParam(defaultValue = "true") boolean count) {
    
        PostResponseDTO postResponseDTO = postService.getPost(postId, count);
    
        return ResponseEntity.ok(postResponseDTO);
    }
}
