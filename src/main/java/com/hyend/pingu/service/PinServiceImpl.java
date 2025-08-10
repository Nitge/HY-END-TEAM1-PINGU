package com.hyend.pingu.service;

import com.hyend.pingu.dto.PinDetailResponseDTO;
import com.hyend.pingu.dto.PinSaveRequestDTO;
import com.hyend.pingu.dto.PinUpdateRequestDTO;
import com.hyend.pingu.entity.PostEntity;
import com.hyend.pingu.entity.UserEntity;
import com.hyend.pingu.repository.PinRepository;
import com.hyend.pingu.repository.PostRepository;
import com.hyend.pingu.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PinServiceImpl implements PinService{

    private final PinRepository pinRepository;
    private final PostRepository postEntity;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long save(PinSaveRequestDTO requestDTO, String username){
        // 사용자 정보 조회
        UserEntity user = UserRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. username=" + username));
        // DTO를 PostEntity로 변환, DB에 저장
        PostEntity post = post
    }

    @Override
    @Transactional
    public PinDetailResponseDTO findPinById(Long pinid){

    }

    @Override
    @Transactional
    public Long update(Long pinid, PinUpdateRequestDTO requestDTO, String username){

    }

    @Override
    @Transactional
    public void delete(Long pinid, String username){

    }
}
