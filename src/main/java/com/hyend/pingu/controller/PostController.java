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

/**
 * PostController.java
 * 게시글(Post) 관련 API 요청을 처리하는 컨트롤러 클래스입니다.
 * 클라이언트로부터 들어오는 게시글 생성, 조회, 수정, 삭제 요청을 받아 Service 계층으로 전달합니다.
 */
@RestController // 이 클래스가 RESTful API의 컨트롤러임을 Spring에게 알립니다.
@RequiredArgsConstructor // final로 선언된 필드의 생성자를 자동으로 생성하여 의존성을 주입합니다.
@RequestMapping("/posts") // 이 컨트롤러의 모든 API는 '/posts' 경로로 시작됩니다.
public class PostController {

    private final PostService postService; // 게시글 관련 비즈니스 로직을 처리하는 서비스

    /**
     * 게시글 목록을 페이징하여 조회합니다. 특정 사용자의 게시글만 조회할 수도 있습니다.
     * @param userId 특정 사용자의 게시글만 보려면 해당 사용자의 ID를, 전체 게시글을 보려면 null로 요청합니다.
     * @param pageRequestDTO 페이지 번호, 페이지 당 개수 등 페이징 정보를 담은 객체입니다.
     * @return 페이징 처리된 게시글 목록과 페이지 정보를 담은 DTO를 반환합니다.
     */
    @GetMapping // HTTP GET 요청을 '/posts' 경로와 매핑합니다.
    public ResponseEntity<PageResultDTO<PostResponseDTO, Post>> getPosts(
            @RequestParam(required = false) Long userId,
            @ModelAttribute PageRequestDTO pageRequestDTO) {

        // 서비스 계층을 호출하여 게시글 목록을 가져옵니다.
        PageResultDTO<PostResponseDTO, Post> posts = postService.getPosts(userId, pageRequestDTO);
        // HTTP 200 OK 상태 코드와 함께 조회된 데이터를 응답 본문에 담아 반환합니다.
        return ResponseEntity.ok(posts);
    }

    /**
     * 주어진 좌표를 기준으로 특정 거리 내에 있는 게시글 목록을 조회합니다.
     * @param longitude 기준점의 경도
     * @param latitude 기준점의 위도
     * @param distance 기준점으로부터의 거리 (미터 단위)
     * @return 기준점 근처의 게시글 DTO 리스트를 반환합니다.
     */
    @GetMapping("/near") // HTTP GET 요청을 '/posts/near' 경로와 매핑합니다.
    public ResponseEntity<List<PostResponseDTO>> getNearPosts(
            @RequestParam Double longitude,
            @RequestParam Double latitude,
            @RequestParam Double distance) {

        // 서비스 계층을 호출하여 근처 게시글 목록을 가져옵니다.
        List<PostResponseDTO> nearPosts = postService.getNearPosts(longitude, latitude, distance);
        // HTTP 200 OK 상태 코드와 함께 조회된 데이터를 반환합니다.
        return ResponseEntity.ok(nearPosts);
    }

    /**
     * 새로운 게시글을 등록합니다. 이미지 파일 업로드를 포함할 수 있습니다.
     * @param postRequestDTO 게시글 정보(제목, 내용 등)와 첨부 파일(이미지)을 담은 DTO.
     * @ModelAttribute는 multipart/form-data 형식의 요청 데이터를 바인딩할 때 사용됩니다.
     * @return 생성된 게시글의 ID를 반환합니다.
     * @throws IOException 파일 처리 중 발생할 수 있는 예외입니다.
     */
    @PostMapping // HTTP POST 요청을 '/posts' 경로와 매핑합니다.
    public ResponseEntity<Long> registerPost(@ModelAttribute PostRequestDTO postRequestDTO) throws IOException {

        // 서비스 계층을 호출하여 게시글을 등록하고, 생성된 게시글의 ID를 받습니다.
        Long registeredPostId = postService.register(postRequestDTO);

        // HTTP 201 Created 상태 코드와 함께 생성된 게시글 ID를 응답 본문에 담아 반환합니다.
        return new ResponseEntity<>(registeredPostId, HttpStatus.CREATED);
    }

    /**
     * 기존 게시글의 정보를 수정합니다.
     * @param postId URL 경로에 포함된, 수정할 게시글의 ID입니다.
     * @param postRequestDTO 수정할 게시글의 정보와 파일을 담은 DTO입니다.
     * @return 수정된 게시글의 ID를 반환합니다.
     * @throws IOException 파일 처리 중 발생할 수 있는 예외입니다.
     */
    @PutMapping("/{postId}") // HTTP PUT 요청을 '/posts/{postId}' 경로와 매핑합니다.
    public ResponseEntity<Long> updatePost(@PathVariable Long postId,
                                           @ModelAttribute PostRequestDTO postRequestDTO) throws IOException {

        // 서비스 계층을 호출하여 게시글을 수정하고, 수정된 게시글의 ID를 받습니다.
        Long updatedPostId = postService.modify(postId, postRequestDTO);
        // HTTP 200 OK 상태 코드와 함께 수정된 ID를 반환합니다.
        return ResponseEntity.ok(updatedPostId);
    }

    /**
     * 특정 게시글을 삭제합니다. (논리적 삭제: 상태를 DELETED로 변경)
     * @param postId URL 경로에 포함된, 삭제할 게시글의 ID입니다.
     * @return 삭제 처리된 게시글의 ID를 반환합니다.
     */
    @DeleteMapping("/{postId}") // HTTP DELETE 요청을 '/posts/{postId}' 경로와 매핑합니다.
    public ResponseEntity<Long> deletePost(@PathVariable Long postId) {

        // 서비스 계층을 호출하여 게시글을 삭제 처리합니다.
        Long deletedPostId = postService.delete(postId);
        // HTTP 200 OK 상태 코드와 함께 삭제된 ID를 반환합니다.
        return ResponseEntity.ok(deletedPostId);
    }

    /**
     * 특정 게시글 하나를 상세 조회합니다.
     * @param postId URL 경로에 포함된, 조회할 게시글의 ID입니다.
     * @param count 조회수 증가 여부를 결정하는 파라미터 (기본값: true).
     * false로 요청 시 조회수를 증가시키지 않습니다.
     * @return 조회된 게시글의 상세 정보를 담은 DTO를 반환합니다.
     */
    @GetMapping("/{postId}") // HTTP GET 요청을 '/posts/{postId}' 경로와 매핑합니다.
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long postId,
                                                   @RequestParam(defaultValue = "true") boolean count) {

        // 서비스 계층을 호출하여 특정 게시글의 정보를 가져옵니다.
        PostResponseDTO postResponseDTO = postService.getPost(postId, count);

        // HTTP 200 OK 상태 코드와 함께 조회된 데이터를 반환합니다.
        return ResponseEntity.ok(postResponseDTO);
    }
}
