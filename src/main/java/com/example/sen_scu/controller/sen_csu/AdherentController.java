package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.dto.sen_csu.AdherentRequest;
import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.service.sen_csu.AdherentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/adherents")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdherentController {
    private final AdherentService adherentService;

    @PostMapping(value = "/create", consumes = {
            "application/json",
            "application/json;charset=UTF-8",
            MediaType.APPLICATION_JSON_VALUE
    }, produces = "application/json")
    public ResponseEntity<?> create(@RequestBody AdherentRequest request, @RequestParam String agentId) {
        try {
            Adherent saved = adherentService.saveWithDependants(request, agentId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Adhérent créé avec succès",
                    "data", Map.of("adherent_id", saved.getId())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<Adherent> adherents = adherentService.getAllAdherents();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Liste des adhérents récupérée avec succès",
                "data", adherents));
    }

    @GetMapping("/by-agent/{agentId}")
    public ResponseEntity<?> getByAgent(@PathVariable("agentId") String agentId) {
        List<Adherent> adherents = adherentService.getAllAdherentsByAgentId(agentId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Liste des adhérents : " + adherents.size(),
                "data", adherents));
    }

    // =========================
    // SYNC (OFFLINE)
    // =========================
    @PostMapping("/sync")
    public ResponseEntity<Void> sync(@RequestBody AdherentRequest request) {

        adherentService.syncAdherent(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable String id) {

        Adherent adherent = adherentService.getAdherentById(id)
                .orElseThrow();

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", adherent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        adherentService.deleteAdherent(id);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody AdherentRequest request) {
        try {
            Adherent updated = adherentService.updateAdherent(id, request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Adhérent mis à jour avec succès",
                    "data", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erreur lors de la mise à jour : " + e.getMessage()));
        }
    }

}
