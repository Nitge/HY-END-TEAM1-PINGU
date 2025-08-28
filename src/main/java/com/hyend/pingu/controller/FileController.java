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

/**
 * FileController.java
 * 서버에 저장된 파일(이미지 등)을 클라이언트에게 제공하는 API를 처리하는 컨트롤러입니다.
 */
@RestController // 이 클래스가 RESTful API의 컨트롤러임을 나타냅니다.
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 파일 ID를 받아 서버에 저장된 실제 파일을 반환합니다.
     * 클라이언트는 이 API를 통해 게시글의 이미지를 불러올 수 있습니다.
     * @param fileId DB에 저장된 파일의 고유 ID.
     * @return 파일 리소스(Resource)를 담은 ResponseEntity. 브라우저는 이 응답을 받아 이미지를 렌더링합니다.
     * @throws FileNotFoundException DB에 파일 정보가 없거나, 실제 파일이 없을 경우 발생할 수 있습니다.
     * @throws MalformedURLException 파일 경로(Path)가 유효한 URL 형식이 아닐 경우 발생합니다.
     */
    @GetMapping("/files/{fileId}") // HTTP GET /files/{fileId}
    public ResponseEntity<Resource> getFile(@PathVariable Long fileId) throws FileNotFoundException, MalformedURLException {
        // 1. FileService를 호출하여 파일 ID에 해당하는 실제 파일의 전체 경로(Path)를 가져옵니다.
        Path path = fileService.getFile(fileId);
        // 2. 경로에서 파일 이름을 추출합니다.
        String fileName = path.getFileName().toString();
        // 3. 파일 이름에서 마지막 '.' 이후의 문자열, 즉 확장자를 추출합니다.
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 4. 파일 경로(Path)를 URI로 변환하고, 이를 기반으로 UrlResource 객체를 생성합니다.
        //    UrlResource는 파일 시스템의 파일을 URL처럼 다룰 수 있게 해주는 Spring의 리소스 클래스입니다.
        UrlResource resource = new UrlResource(path.toUri());

        // 5. ResponseEntity를 사용하여 최종 HTTP 응답을 구성합니다.
        return ResponseEntity.ok()
                // HTTP 응답 헤더(HttpHeaders)에 'Content-Type'을 설정합니다.
                // Content-Type은 브라우저에게 이 데이터가 이미지인지, 텍스트인지 등을 알려주는 중요한 정보입니다.
                // (예: Content-Type: image/jpeg)
                .header(HttpHeaders.CONTENT_TYPE, ContentType.fromExtension(ext).toString())
                // 응답 본문(body)에 실제 파일 리소스(resource)를 담아 클라이언트에게 전송합니다.
                .body(resource);
    }
}
