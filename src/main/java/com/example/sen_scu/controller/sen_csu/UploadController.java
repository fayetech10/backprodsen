package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.service.sen_csu.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class UploadController {

    private final StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {

        String filename = storageService.saveFile(file);

        return ResponseEntity.ok(Map.of("filename", filename, "url", filename));
    }
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(
            @PathVariable("filename") String filename) {

        Resource file = storageService.loadFile(filename);

        if (file == null || !file.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = "application/octet-stream";

        try {
            contentType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException e) {
            // fallback silencieux
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + file.getFilename() + "\""
                )
                .body(file);
    }

}
