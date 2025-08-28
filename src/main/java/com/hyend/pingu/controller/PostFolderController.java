package com.hyend.pingu.controller;

import com.hyend.pingu.dto.PostFolderDTO;
import com.hyend.pingu.service.PostFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PostFolderController.java
 * 게시글 폴더(PostFolder)와 관련된 API 요청을 처리하는 컨트롤러입니다.
 * 사용자는 이 API를 통해 자신의 게시글 폴더를 생성, 조회, 수정, 삭제할 수 있습니다.
 */
@RestController // 이 클래스가 RESTful API의 컨트롤러임을 나타냅니다.
@RequestMapping("/folders") // 이 컨트롤러의 모든 API는 '/folders' 경로로 시작됩니다.
@RequiredArgsConstructor
public class PostFolderController {

    private final PostFolderService postFolderService;

    /**
     * 특정 사용자가 생성한 모든 게시글 폴더 목록을 조회합니다.
     * @param userId 폴더 목록을 조회할 사용자의 ID.
     * @return 해당 사용자의 폴더 정보 DTO 리스트를 반환합니다.
     */
    @GetMapping("/user/{userId}") // HTTP GET /folders/user/{userId}
    public ResponseEntity<List<PostFolderDTO>> getFoldersByUser(@PathVariable Long userId) {
        // 서비스 계층을 호출하여 특정 사용자의 폴더 목록을 가져옵니다.
        List<PostFolderDTO> postFolderDTOs = postFolderService.getPostFoldersByUser(userId);
        // HTTP 200 OK 상태 코드와 함께 조회된 목록을 응답합니다.
        return ResponseEntity.ok(postFolderDTOs);
    }

    /**
     * 새로운 게시글 폴더를 생성합니다.
     * @param postFolderDTO 생성할 폴더의 정보(이름, 소유자 ID 등)를 담은 DTO.
     * @return 생성된 폴더의 고유 ID를 반환합니다.
     */
    @PostMapping // HTTP POST /folders
    public ResponseEntity<Long> createFolder(@RequestBody PostFolderDTO postFolderDTO) {
        // 서비스 계층을 호출하여 폴더를 생성하고, 생성된 ID를 받습니다.
        Long folderId = postFolderService.createFolder(postFolderDTO);
        // HTTP 200 OK 상태 코드와 함께 생성된 ID를 응답합니다. (상황에 따라 201 Created도 가능)
        return ResponseEntity.ok(folderId);
    }

    /**
     * 기존 게시글 폴더의 정보를 수정합니다.
     * @param postFolderDTO 수정할 폴더의 ID와 변경할 내용이 담긴 DTO.
     * @return 수정된 폴더의 ID를 반환합니다.
     */
    @PutMapping // HTTP PUT /folders
    public ResponseEntity<Long> updateFolder(@RequestBody PostFolderDTO postFolderDTO) {
        // 서비스 계층을 호출하여 폴더 정보를 수정합니다.
        Long folderId = postFolderService.modifyFolder(postFolderDTO);
        return ResponseEntity.ok(folderId);
    }

    /**
     * 특정 게시글 폴더를 삭제합니다.
     * @param folderId 삭제할 폴더의 ID.
     * @return 삭제 처리된 폴더의 ID를 반환합니다.
     */
    @DeleteMapping("/{folderId}") // HTTP DELETE /folders/{folderId}
    public ResponseEntity<Long> deleteFolder(@PathVariable Long folderId) {
        // 서비스 계층을 호출하여 폴더를 삭제합니다.
        postFolderService.deleteFolder(folderId);
        // HTTP 200 OK 상태 코드와 함께 삭제된 ID를 응답합니다.
        return ResponseEntity.ok(folderId);
    }

}
