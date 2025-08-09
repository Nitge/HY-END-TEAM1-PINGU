package com.hyend.pingu.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FileInfo {

    private String storePath;

    private String storeFileName;

    private String originalFileName;

    private String ext;

    private Long size;

}
