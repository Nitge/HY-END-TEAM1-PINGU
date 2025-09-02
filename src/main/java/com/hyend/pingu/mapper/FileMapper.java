package com.hyend.pingu.mapper;

import com.hyend.pingu.dto.FileResponseDTO;
import com.hyend.pingu.entity.File;
import com.hyend.pingu.enumeration.ContentType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class FileMapper {

    public FileResponseDTO EntityToDto(File file) {

        String uriString = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files/")
                .path(file.getId().toString())
                .toUriString();

        return FileResponseDTO.builder()
                .fileId(file.getId())
                .name(file.getFileInfo().getOriginalFileName())
                .url(uriString)
                .fileSize(file.getFileInfo().getSize())
                .contentType(ContentType.fromExtension(file.getFileInfo().getExt()))
                .createdAt(file.getCreatedAt())
                .updatedAt(file.getUpdatedAt())
                .build();
    }
}
