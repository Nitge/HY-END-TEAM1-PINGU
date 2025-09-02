package com.hyend.pingu.dto;

import com.hyend.pingu.enumeration.ContentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FileResponseDTO {

    private Long fileId;

    private String name;

    private String url;

    private ContentType contentType;

    private Long fileSize;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
