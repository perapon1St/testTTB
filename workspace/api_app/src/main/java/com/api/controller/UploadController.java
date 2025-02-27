package com.api.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.nio.file.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend access
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private static final String UPLOAD_DIR = "uploads/";

    static {
        try {
            Path path = Paths.get(UPLOAD_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            logger.error("Failed to create uploads directory", e);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "File is empty!"));
            }

            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = getFileExtension(originalFilename);
            String fileName = UUID.randomUUID().toString() + "." + fileExtension;
            Path path = Paths.get(UPLOAD_DIR, fileName);

            // Save file to local storage
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Construct file URL (optional: if serving files from the backend)
            String fileUrl = "/uploads/" + fileName;  // This is the relative path

            // Return file name & path in response
            response.put("fileName", fileName);
            response.put("filePath", fileUrl);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("File upload failed!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "File upload failed!"));
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return (lastIndex == -1) ? "" : fileName.substring(lastIndex + 1);
    }
}
