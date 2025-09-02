package com.hyend.pingu.util;

import com.hyend.pingu.entity.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStoreUtil {

    @Value("${store.path}")
    private String storePath;

    public FileInfo storeFile(MultipartFile file) throws IOException {

        String originalFileName = file.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        String ext = extractExt(originalFileName);
        long size = file.getSize();

        file.transferTo(new File(storePath + "/" + storeFileName));

        return new FileInfo(storePath, storeFileName, originalFileName, ext, size);
    }

    public List<FileInfo> storeFiles(List<MultipartFile> files) throws IOException {

        List<FileInfo> fileInfos = new ArrayList<>();

        for (MultipartFile file : files) {
            fileInfos.add(storeFile(file));
        }

        return fileInfos;
    }

    private String createStoreFileName(String originalFileName) {

        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFileName);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {

        return originalFileName.
                substring(originalFileName.lastIndexOf(".") + 1);
    }
}
