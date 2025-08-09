package com.hyend.pingu.service;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public interface FileService {

    public Path getFile(Long fileId) throws FileNotFoundException;

}
