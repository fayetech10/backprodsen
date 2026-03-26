package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.dto.sen_csu.PhotoMetadata;
import com.example.sen_scu.service.sen_csu.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PhotoController {

    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<List<PhotoMetadata>> getAllPhotos() {
        return ResponseEntity.ok(storageService.listPhotos());
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
        Resource resource = storageService.loadFile(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // General image type, could be refined
                .body(resource);
    }

    @GetMapping("/download")
    public ResponseEntity<Void> downloadPhotosByPeriod(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String type,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=photos.zip");

        storageService.writeZip(startDate, endDate, type, response.getOutputStream());
        response.flushBuffer();
        return null;
    }
}
