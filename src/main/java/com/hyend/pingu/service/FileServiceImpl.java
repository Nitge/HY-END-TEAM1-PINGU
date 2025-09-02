package com.hyend.pingu.service;

import com.hyend.pingu.entity.FileInfo;
import com.hyend.pingu.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FileServiceImpl.java
 * FileService 인터페이스의 실제 구현체입니다.
 * 파일 조회와 관련된 비즈니스 로직을 담당합니다.
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    /**
     * 파일 ID를 받아 DB에서 파일 정보를 조회하고, 실제 파일이 저장된 전체 경로를 반환합니다.
     * @param fileId 조회할 파일의 ID.
     * @return 서버 내 파일의 전체 경로를 나타내는 Path 객체.
     * @throws FileNotFoundException 해당 ID를 가진 파일 정보가 DB에 없을 경우 발생합니다.
     */
    @Override
    public Path getFile(Long fileId) throws FileNotFoundException {
        // 1. FileRepository를 통해 fileId에 해당하는 File 엔티티를 조회합니다.
        //    만약 엔티티가 존재하지 않으면 "File not found" 메시지와 함께 예외를 발생시킵니다.
        // 2. 조회된 File 엔티티로부터 연관된 FileInfo 임베디드 객체를 가져옵니다.
        FileInfo fileInfo = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found"))
                .getFileInfo();

        // 3. FileInfo에 저장된 폴더 경로(getStorePath)와 실제 저장된 파일명(getStoreFileName)을 조합하여
        //    파일의 전체 경로를 나타내는 Path 객체를 생성하고 반환합니다.
        //    (예: "/path/to/files/" + "abc-123.jpg" -> "/path/to/files/abc-123.jpg")
        return Paths.get(fileInfo.getStorePath() + "/" + fileInfo.getStoreFileName());
    }

}
