package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.PersonneCharge;
import com.example.sen_scu.repository.sen_csu.AdherentRepository;
import com.example.sen_scu.repository.sen_csu.PersonneChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import com.example.sen_scu.dto.sen_csu.PhotoMetadata;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class StorageService {
    @Value("${app.storage.path}")
    private String storagePath;

    private final AdherentRepository adherentRepository;
    private final PersonneChargeRepository personneChargeRepository;

    public String saveFile(MultipartFile file) {
        // ... previous implementation remains or is adapted
// (Keeping original logic for saveFile as it was before my previous edits)
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Fichier vide");
            }
            Files.createDirectories(Paths.get(storagePath));
            String original = file.getOriginalFilename();
            String ext = original.substring(original.lastIndexOf("."));
            String filename = UUID.randomUUID() + ext;
            Path filePath = Paths.get(storagePath, filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (Exception e) {
            throw new RuntimeException("Impossible d'enregistrer le fichier : " + e.getMessage());
        }
    }

    public Resource loadFile(String filename) {
        try {
            Path path = Paths.get(storagePath).resolve(filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists())
                return resource;
            throw new RuntimeException("Fichier introuvable");
        } catch (Exception e) {
            throw new RuntimeException("Erreur récupération fichier : " + filename);
        }
    }

    public List<PhotoMetadata> listPhotos() {
        List<PhotoMetadata> list = new ArrayList<>();
        try {
            Path root = Paths.get(storagePath);
            if (!Files.exists(root))
                return list;

            List<Adherent> adherents = adherentRepository.findAll();
            List<PersonneCharge> pcList = personneChargeRepository.findAll();

            Files.list(root).forEach(path -> {
                try {
                    String filename = path.getFileName().toString();
                    BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                    
                    String type = determineType(filename, adherents, pcList);

                    list.add(new PhotoMetadata(
                            filename,
                            type,
                            attr.size(),
                            attr.lastModifiedTime().toMillis()));
                } catch (IOException e) {
                    // Skip files that can't be read
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du listage des photos", e);
        }
        return list;
    }

    private String determineType(String filename, List<Adherent> adherents, List<PersonneCharge> pcList) {
        for (Adherent a : adherents) {
            if (filename.equals(a.getPhoto())) return "profile";
            if (filename.equals(a.getPhotoRecto())) return "recto";
            if (filename.equals(a.getPhotoVerso())) return "verso";
        }
        for (PersonneCharge pc : pcList) {
            if (filename.equals(pc.getPhoto())) return "profile";
            if (filename.equals(pc.getPhotoRecto())) return "recto";
            if (filename.equals(pc.getPhotoVerso())) return "verso";
        }
        return "other";
    }

    public void writeZip(LocalDate start, LocalDate end, String typeFilter, OutputStream out) throws IOException {
        Path root = Paths.get(storagePath);
        if (!Files.exists(root))
            return;

        List<Adherent> adherents = adherentRepository.findAll();
        List<PersonneCharge> pcList = personneChargeRepository.findAll();

        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            Files.list(root).forEach(path -> {
                try {
                    String filename = path.getFileName().toString();
                    BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                    LocalDate modifiedDate = attr.lastModifiedTime().toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    String type = determineType(filename, adherents, pcList);

                    boolean dateMatch = (start == null || !modifiedDate.isBefore(start)) &&
                                        (end == null || !modifiedDate.isAfter(end));
                    
                    boolean typeMatch = typeFilter == null || "all".equalsIgnoreCase(typeFilter) || typeFilter.equalsIgnoreCase(type);

                    if (dateMatch && typeMatch) {
                        ZipEntry zipEntry = new ZipEntry(filename);
                        zos.putNextEntry(zipEntry);
                        Files.copy(path, zos);
                        zos.closeEntry();
                    }
                } catch (IOException e) {
                    // Skip or log
                }
            });
        }
    }
}
