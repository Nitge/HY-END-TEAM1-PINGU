package com.hyend.pingu.controller;

import com.hyend.pingu.dto.PostFolderDTO;
import com.hyend.pingu.service.PostFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders")
@RequiredArgsConstructor
public class PostFolderController {

    private final PostFolderService postFolderService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostFolderDTO>> getFoldersByUser(@PathVariable Long userId) {

        List<PostFolderDTO> postFolderDTOs = postFolderService.getPostFoldersByUser(userId);

        return ResponseEntity.ok(postFolderDTOs);
    }

    @PostMapping
    public ResponseEntity<Long> createFolder(@RequestBody PostFolderDTO postFolderDTO) {

        Long folderId = postFolderService.createFolder(postFolderDTO);

        return ResponseEntity.ok(folderId);
    }

    @PutMapping
    public ResponseEntity<Long> updateFolder(@RequestBody PostFolderDTO postFolderDTO) {
        Long folderId = postFolderService.modifyFolder(postFolderDTO);
        return ResponseEntity.ok(folderId);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Long> deleteFolder(@PathVariable Long folderId) {

        postFolderService.deleteFolder(folderId);

        return ResponseEntity.ok(folderId);
    }

}
