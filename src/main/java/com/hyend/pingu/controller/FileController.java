package com.hyend.pingu.controller;

import com.hyend.pingu.enumeration.ContentType;
import com.hyend.pingu.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/files/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable Long fileId) throws FileNotFoundException, MalformedURLException {

        Path path = fileService.getFile(fileId);
        String fileName = path.getFileName().toString();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        UrlResource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, ContentType.fromExtension(ext).toString())
                .body(resource);
    }
}
