package com.hyend.pingu.dto;

import com.hyend.pingu.enumeration.ContentType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileResponseDTO {

    private Long fileId;

    private String name;

    private String url;

    private ContentType contentType;

    private Long fileSize;
}
