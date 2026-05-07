package com.taskmanager.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    private final String UPLOAD_DIR = "uploads/";

    public String uploadFile(MultipartFile file) throws IOException {

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        String path = UPLOAD_DIR + file.getOriginalFilename();
        file.transferTo(new File(path));

        return path;
    }

public Resource downloadFile(String fileName) throws Exception {

    Path filePath = Paths.get("uploads").resolve(fileName).normalize();

    Resource resource = new UrlResource(filePath.toUri());

    if (!resource.exists()) {
        throw new RuntimeException("File not found");
    }

    return resource;
}

}