package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.dto.sen_csu.PersonneChargeRequest;
import com.example.sen_scu.model.sen_csu.PersonneCharge;
import com.example.sen_scu.service.sen_csu.PersonneEnchargeSevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/adherents")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PersonneEnchargeController {

    private final PersonneEnchargeSevice personneChargeService;

    /* ==================== PERSONNES EN CHARGE ==================== */

    /**
     * Ajouter une personne en charge à un adhérent
     * POST /api/adherents/{adherentId}/personnes-charge
     */
    @PostMapping("/{adherentId}/personnes-charge")
    public ResponseEntity<?> addPersonneCharge(
            @PathVariable String adherentId,
            @Valid @RequestBody PersonneChargeRequest request) {
        try {
            PersonneCharge saved = personneChargeService.savePersonneChargeRequest(request, adherentId);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erreur serveur : " + e.getMessage()
            ));
        }
    }

    /**
     * Mettre à jour une personne en charge
     * PUT /api/adherents/{adherentId}/personnes-charge/{pcId}
     */
    @PutMapping("/{adherentId}/personnes-charge/{pcId}")
    public ResponseEntity<?> updatePersonneCharge(
            @PathVariable String adherentId,
            @PathVariable String pcId,
            @Valid @RequestBody PersonneChargeRequest request) {
        try {
            PersonneCharge updated = personneChargeService.update(adherentId, pcId, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erreur serveur : " + e.getMessage()
            ));
        }
    }

    /**
     * Supprimer une personne en charge
     * DELETE /api/adherents/{adherentId}/personnes-charge/{pcId}
     */
    @DeleteMapping("/{adherentId}/personnes-charge/{pcId}")
    public ResponseEntity<?> deletePersonneCharge(
            @PathVariable String adherentId,
            @PathVariable String pcId) {
        try {
            personneChargeService.delete(adherentId, pcId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erreur serveur : " + e.getMessage()
            ));
        }
    }
}