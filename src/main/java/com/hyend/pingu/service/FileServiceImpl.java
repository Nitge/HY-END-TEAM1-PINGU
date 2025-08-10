package com.hyend.pingu.service;

import com.hyend.pingu.entity.FileInfo;
import com.hyend.pingu.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public Path getFile(Long fileId) throws FileNotFoundException {

        FileInfo fileInfo = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found"))
                .getFileInfo();

        return Paths.get(fileInfo.getStorePath() + fileInfo.getStoreFileName());
    }

}
