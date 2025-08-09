package com.hyend.pingu.mapper;

import com.hyend.pingu.dto.FileResponseDTO;
import com.hyend.pingu.entity.FileEntity;
import com.hyend.pingu.enumeration.ContentType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class FileMapper {

    public FileResponseDTO EntityToDto(FileEntity fileEntity) {

        String uriString = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files")
                .path(fileEntity.getFileId().toString())
                .toUriString();

        return FileResponseDTO.builder()
                .fileId(fileEntity.getFileId())
                .name(fileEntity.getFileInfo().getOriginalFileName())
                .url(uriString)
                .fileSize(fileEntity.getFileInfo().getSize())
                .contentType(ContentType.fromExtension(fileEntity.getFileInfo().getExt()))
                .build();
    }
}
