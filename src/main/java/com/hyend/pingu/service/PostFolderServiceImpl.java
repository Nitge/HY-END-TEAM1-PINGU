package com.hyend.pingu.service;

import com.hyend.pingu.dto.PostFolderDTO;
import com.hyend.pingu.entity.PostFolder;
import com.hyend.pingu.entity.User;
import com.hyend.pingu.mapper.PostFolderMapper;
import com.hyend.pingu.repository.PostFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PostFolderServiceImpl.java
 * PostFolderService 인터페이스의 실제 구현체입니다.
 * 게시글 폴더의 생성, 조회, 수정, 삭제와 관련된 핵심 비즈니스 로직을 담당합니다.
 */
@Service
@RequiredArgsConstructor
public class PostFolderServiceImpl implements PostFolderService {

    private final PostFolderRepository postFolderRepository;
    private final PostFolderMapper postFolderMapper;


    /**
     * 특정 사용자가 소유한 모든 게시글 폴더 목록을 조회합니다.
     * @param userId 폴더 목록을 조회할 사용자의 ID.
     * @return 조회된 폴더 정보 DTO 리스트.
     */
    @Override
    public List<PostFolderDTO> getPostFoldersByUser(Long userId) {
        // 1. Repository 메소드에 전달하기 위해 userId만으로 User 객체를 생성합니다.
        //    JPA는 연관관계 매핑 시 ID만으로도 객체를 인식할 수 있습니다.
        User user = User.builder()
                .id(userId)
                .build();

        // 2. PostFolderRepository를 통해 특정 사용자의 모든 폴더 엔티티를 조회합니다.
        List<PostFolder> folders = postFolderRepository.findByUser(user);

        // 3. 조회된 엔티티 리스트를 Stream API와 Mapper를 사용하여 DTO 리스트로 변환합니다.
        List<PostFolderDTO> postFolderDTOs = folders.stream()
                .map(postFolderMapper::entityToDto).toList();

        return postFolderDTOs;
    }

    /**
     * DTO 정보를 바탕으로 새로운 게시글 폴더를 생성하고 DB에 저장합니다.
     * @param postFolderDTO 생성할 폴더의 정보(이름, 사용자 ID)가 담긴 DTO.
     * @return 생성된 폴더의 고유 ID.
     */
    @Override
    public Long createFolder(PostFolderDTO postFolderDTO) {
        // 1. DTO에서 userId를 가져와 폴더의 소유자(User) 엔티티를 생성합니다.
        User user = User.builder()
                .id(postFolderDTO.getUserId())
                .build();

        // 2. 폴더 소유자와 폴더 이름을 사용하여 새로운 PostFolder 엔티티를 생성합니다.
        PostFolder postFolder = PostFolder.builder()
                .user(user)
                .name(postFolderDTO.getName())
                .build();

        // 3. 생성된 엔티티를 DB에 저장합니다.
        postFolderRepository.save(postFolder);

        // 4. 저장된 엔티티의 ID를 반환합니다.
        return postFolder.getId();
    }

    /**
     * 기존 폴더의 정보를 수정합니다. (폴더 이름, 폴더에 속한 게시글 목록 변경 등)
     * @param postFolderDTO 수정할 폴더의 ID와 변경할 내용이 담긴 DTO.
     * @return 수정된 폴더의 ID.
     */
    @Override
    public Long modifyFolder(PostFolderDTO postFolderDTO) {
        // 1. DTO의 ID로 기존 PostFolder 엔티티를 DB에서 조회합니다. 없으면 예외를 발생시킵니다.
        PostFolder postFolder = postFolderRepository.findById(postFolderDTO.getId())
                .orElseThrow(() -> new RuntimeException("폴더가 없음"));

        // 2. 엔티티 내부의 메소드를 호출하여 폴더 이름을 변경합니다.
        postFolder.changeName(postFolderDTO.getName());
        // 3. 엔티티 내부의 메소드를 호출하여 폴더에 속한 게시글 목록을 변경합니다.
        postFolder.changePosts(postFolderDTO);

        // 4. 변경된 엔티티를 저장합니다. JPA의 Dirty Checking에 의해 변경된 필드만 UPDATE 쿼리가 실행됩니다.
        postFolderRepository.save(postFolder);

        return postFolder.getId();
    }

    /**
     * 특정 ID를 가진 폴더를 DB에서 삭제합니다.
     * @param folderId 삭제할 폴더의 ID.
     * @return 삭제 처리된 폴더의 ID.
     */
    @Override
    public Long deleteFolder(Long folderId) {
        // Repository를 통해 ID에 해당하는 폴더를 DB에서 삭제합니다.
        postFolderRepository.deleteById(folderId);
        return folderId;
    }
}
