package com.hyend.pingu.service;

import com.hyend.pingu.dto.PageRequestDTO;
import com.hyend.pingu.dto.PageResultDTO;
import com.hyend.pingu.dto.PostRequestDTO;
import com.hyend.pingu.dto.PostResponseDTO;
import com.hyend.pingu.entity.File;
import com.hyend.pingu.entity.FileInfo;
import com.hyend.pingu.entity.Post;
import com.hyend.pingu.enumeration.Scope;
import com.hyend.pingu.mapper.PostMapper;
import com.hyend.pingu.repository.PostRepository;
import com.hyend.pingu.util.FileStoreUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * PostServiceImpl.java
 * PostService 인터페이스의 실제 구현체입니다.
 * 데이터베이스와의 상호작용, 데이터 가공, 파일 처리 등
 * 게시글과 관련된 핵심 비즈니스 로직을 담당합니다.
 */
@Service // 이 클래스가 서비스 계층의 컴포넌트임을 Spring에게 알립니다.
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    // 의존성 주입: final 키워드와 @RequiredArgsConstructor를 통해 생성자 주입이 이루어집니다.
    private final PostRepository postRepository; // 게시글 데이터베이스 작업을 위한 리포지토리
    private final PostMapper postMapper; // Entity와 DTO 간의 변환을 담당하는 매퍼
    private final FileStoreUtil fileStoreUtil; // 파일 저장 및 관리를 위한 유틸리티 클래스
    private final GeometryFactory geometryFactory; // 좌표 데이터를 Point 객체로 생성하기 위한 팩토리

    /**
     * 게시글 목록을 페이징하여 조회합니다.
     * @param userId 특정 사용자의 게시글을 조회할 경우 해당 사용자의 ID, 아닐 경우 null.
     * @param pageRequestDTO 페이징 요청 정보 (페이지 번호, 사이즈).
     * @return 페이징된 결과를 담은 PageResultDTO 객체.
     */
    @Override
    public PageResultDTO<PostResponseDTO, Post> getPosts(Long userId, PageRequestDTO pageRequestDTO) {
        // 1. PageRequestDTO를 사용하여 Pageable 객체를 생성합니다. 정렬 기준은 'createdAt'의 내림차순(최신순)입니다.
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("createdAt").descending());
        Page<Post> postEntities;

        // 2. userId가 null이 아닌 경우, 특정 사용자의 게시글만 조회합니다.
        if(userId != null) {
            postEntities = postRepository.findAllByUserId(userId, pageable);
        } else { // userId가 null인 경우, 모든 사용자의 게시글을 조회합니다.
            postEntities = postRepository.findAll(pageable);
        }

        // 3. 조회된 Page<Post> 엔티티를 PageResultDTO로 변환하여 반환합니다.
        //    변환 함수로는 PostMapper의 entityToDto 메소드를 사용합니다.
        return new PageResultDTO<>(postEntities, postMapper::entityToDto);
    }

    /**
     * 주어진 좌표와 거리 내에 있는 게시글 목록을 조회합니다.
     * @param longitude 기준점 경도
     * @param latitude 기준점 위도
     * @param distance 검색할 반경 (미터 단위)
     * @return 조건에 맞는 게시글 DTO 리스트
     */
    @Override
    public List<PostResponseDTO> getNearPosts(Double longitude, Double latitude, double distance) {
        // 1. JTS(Java Topology Suite) 라이브러리의 GeometryFactory를 사용하여 경도, 위도 좌표로 Point 객체를 생성합니다.
        Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        // 2. PostRepository에 정의된 공간 쿼리(Spatial Query) 메소드를 호출하여 근처 게시글 엔티티 목록을 가져옵니다.
        List<Post> nearPosts = postRepository.findNear(location, distance);

        // 3. 조회된 엔티티 리스트를 Stream API를 사용해 DTO 리스트로 변환하여 반환합니다.
        return nearPosts.stream()
                .map(postMapper::entityToDto)
                .toList();
    }

    /**
     * DTO로부터 받은 정보로 새로운 게시글을 생성합니다.
     * @param postRequestDTO 생성할 게시글의 정보와 파일이 담긴 DTO
     * @return 생성된 게시글의 ID
     * @throws IOException 파일 저장 중 발생할 수 있는 예외
     */
    @Override
    public Long register(PostRequestDTO postRequestDTO) throws IOException {
        // 1. Mapper를 사용하여 요청 DTO를 Post 엔티티로 변환합니다.
        Post post = postMapper.dtoToEntity(postRequestDTO);
        // 2. 파일 처리 및 DB 저장을 담당하는 private 메소드를 호출합니다.
        return setFilesAndSave(postRequestDTO, post);
    }

    /**
     * 기존 게시글의 내용을 수정합니다.
     * @param postId 수정할 게시글의 ID
     * @param postRequestDTO 수정할 내용이 담긴 DTO
     * @return 수정된 게시글의 ID
     * @throws IOException 파일 저장/수정 중 발생할 수 있는 예외
     */
    @Override
    public Long modify(Long postId, PostRequestDTO postRequestDTO) throws IOException {
        // 1. postId로 DB에서 기존 Post 엔티티를 조회합니다. 없으면 예외를 발생시킵니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 Entity가 없습니다."));

        // 2. DTO에 수정할 내용이 있는 경우에만 엔티티의 상태를 변경합니다. (부분 업데이트 지원)
        if (postRequestDTO.getTitle() != null) {
            post.changeTitle(postRequestDTO.getTitle());
        }
        if (postRequestDTO.getContent() != null) {
            post.changeContent(postRequestDTO.getContent());
        }
        if (postRequestDTO.getScope() != null && !postRequestDTO.getScope().isBlank()) {
            post.changeScope(Scope.valueOf(postRequestDTO.getScope()));
        }
        if (postRequestDTO.getLongitude() != null && postRequestDTO.getLatitude() != null ) {
            Point location = geometryFactory.createPoint(
                    new Coordinate(postRequestDTO.getLongitude(), postRequestDTO.getLatitude())
            );
            post.changeLocation(location);
        }

        // 3. 파일 처리 및 DB 저장을 담당하는 private 메소드를 호출합니다.
        //    @Transactional에 의해 이 메소드가 끝나면 변경된 내용이 DB에 자동으로 반영(UPDATE)됩니다. (Dirty Checking)
        return setFilesAndSave(postRequestDTO, post);
    }

    /**
     * 게시글을 삭제합니다. (실제 DB에서 행을 지우지 않고, 상태만 변경)
     * @param postId 삭제할 게시글의 ID
     * @return 삭제 처리된 게시글의 ID
     */
    @Override
    public Long delete(Long postId) {
        // 1. postId로 DB에서 Post 엔티티를 조회합니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 Entity가 없습니다."));

        // 2. 'Soft Delete'를 위해 게시글의 상태(scope)를 DELETED로 변경합니다.
        //    이렇게 하면 데이터를 복구하거나 관리자가 확인할 수 있는 장점이 있습니다.
        post.changeScope(Scope.DELETED);
        // 3. 변경된 상태를 DB에 저장합니다.
        postRepository.save(post);

        return postId;
    }

    /**
     * 게시글 하나를 상세 조회하고, 필요한 경우 조회수를 1 증가시킵니다.
     * @param postId 조회할 게시글의 ID
     * @param count 조회수를 증가시킬지 여부
     * @return 조회된 게시글의 상세 정보를 담은 DTO
     */
    @Override
    public PostResponseDTO getPost(Long postId, boolean count) {
        // 1. postId로 DB에서 Post 엔티티를 조회합니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 Entity가 없습니다."));

        // 2. count 파라미터가 true인 경우에만 조회수를 1 증가시킵니다.
        if(count) {
            post.increaseViewCount();
            postRepository.save(post); // 변경된 조회수를 DB에 반영
        }

        // 3. 조회된 엔티티를 DTO로 변환하여 반환합니다.
        return postMapper.entityToDto(post);
    }

    /**
     * [private] 게시글에 첨부된 파일들을 처리하고, 최종적으로 게시글을 DB에 저장하는 공통 메소드입니다.
     * @param postRequestDTO 파일 정보가 담긴 요청 DTO
     * @param post DB에 저장하거나 수정할 Post 엔티티
     * @return 저장된 게시글의 ID
     * @throws IOException 파일 저장 중 발생할 수 있는 예외
     */
    private Long setFilesAndSave(PostRequestDTO postRequestDTO, Post post) throws IOException {
        // 1. 요청 DTO에 파일이 존재하는지 확인합니다.
        if (postRequestDTO.getFiles() != null && !postRequestDTO.getFiles().isEmpty()) {
            // 2. FileStoreUtil을 사용해 실제 파일들을 서버의 특정 위치에 저장하고, 저장된 파일 정보를(FileInfo) 받아옵니다.
            List<FileInfo> fileInfos = fileStoreUtil.storeFiles(postRequestDTO.getFiles());

            // 3. 받아온 파일 정보(FileInfo)를 File 엔티티로 변환합니다.
            List<File> fileEntities = fileInfos.stream()
                    .map(
                            fileInfo -> File.builder()
                                    .post(post) // File 엔티티와 Post 엔티티의 연관관계를 설정합니다.
                                    .fileInfo(fileInfo)
                                    .build()
                    )
                    .toList();

            // 4. Post 엔티티에 파일 엔티티 리스트를 설정합니다. (JPA 연관관계 편의 메소드)
            post.changeFiles(fileEntities);
        }

        // 5. 파일 정보가 포함된 최종 Post 엔티티를 DB에 저장(INSERT 또는 UPDATE)합니다.
        postRepository.save(post);
        return post.getId();
    }
}
