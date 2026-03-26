package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.Assure;
import com.example.sen_scu.model.sen_csu.Beneficiaire;
import com.example.sen_scu.service.sen_csu.AdherentService;
import com.example.sen_scu.service.sen_csu.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/excel")
public class ExcelController {

    private final AdherentService adherentService;

    private final ExcelService excelService;

    @GetMapping("/adherents")
    public ResponseEntity<InputStreamResource> downloadAllAdherents(

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate)

            throws IOException {

        List<Adherent> adherents;
        if (startDate != null && endDate != null) {
            adherents = adherentService.getAllAdherentsByDateRange(startDate.atStartOfDay(), endDate.atStartOfDay());
        } else {
            adherents = adherentService.getAllAdherents();
        }

        // La bonne méthode !
        ByteArrayInputStream excel = excelService.adherentsToExcel(adherents);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=adherents.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excel));
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importer(@RequestParam("file") MultipartFile file) throws IOException {
        excelService.importExcel(file, file.getOriginalFilename());
        return ResponseEntity.ok("Importation réussie");
    }

    @PostMapping(value = "/importBenef", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importerBenef(@RequestParam("file") MultipartFile file) throws IOException {
        excelService.importExcelBenef(file);
        return ResponseEntity.ok("Importation réussie");
    }

    @GetMapping("/assures")
    public ResponseEntity<?> getAllAssures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Page<Assure> assures = excelService.getAssures(page, size);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Liste des adhérents récupérée avec succès",
                "data", assures.getContent(),
                "currentPage", assures.getNumber(),
                "totalItems", assures.getTotalElements(),
                "totalPages", assures.getTotalPages()));
    }

    @GetMapping("/beneficiaire")
    public ResponseEntity<?> getallBenefs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Page<Beneficiaire> assures = excelService.getBenef(page, size);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Liste des adhérents récupérée avec succès",
                "data", assures.getContent(),
                "currentPage", assures.getNumber(),
                "totalItems", assures.getTotalElements(),
                "totalPages", assures.getTotalPages()));
    }

    @PutMapping("/assures/{id}/carte")
    public ResponseEntity<?> updateCarteStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String carteAssure = body.getOrDefault("carteAssure", "");
        String dateRemise = body.getOrDefault("dateRemise", "");
        Assure updated = excelService.updateCarteStatus(id, carteAssure, dateRemise);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Carte mise à jour avec succès",
                "data", updated));
    }

    @PostMapping(value = "/importCartes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importCartes(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> result = excelService.importCartes(file);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Importation des cartes terminée",
                "updated", result.get("updated"),
                "notFound", result.get("notFound")));
    }

    @GetMapping("/imports")
    public ResponseEntity<?> getDistinctImports() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", excelService.getDistinctImportNames()));
    }

    @GetMapping("/assures/batch/{batchName}")
    public ResponseEntity<?> getAssuresByBatch(
            @PathVariable String batchName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Page<Assure> assures = excelService.getAssuresByImportName(batchName, page, size);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", assures.getContent(),
                "currentPage", assures.getNumber(),
                "totalItems", assures.getTotalElements(),
                "totalPages", assures.getTotalPages()));
    }

    @GetMapping("/agents")
    public ResponseEntity<?> getDistinctAgents() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", excelService.getDistinctAgents()));
    }

    @PostMapping("/assures/bulk-validate/{agentName}")
    public ResponseEntity<?> bulkValidateByAgent(@PathVariable String agentName) {
        int count = excelService.bulkValidateByAgent(agentName);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", count + " carte(s) validée(s) pour l'agent " + agentName,
                "validated", count));
    }

}
