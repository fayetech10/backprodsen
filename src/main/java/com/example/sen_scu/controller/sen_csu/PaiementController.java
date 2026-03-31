package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.dto.sen_csu.PaiementRequest;
import com.example.sen_scu.model.sen_csu.PaiementCotisation;
import com.example.sen_scu.service.sen_csu.PaiementService;
import com.example.sen_scu.service.sen_csu.exception.AdherentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {
    private final PaiementService paiementService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody PaiementRequest request) {
        try {
            PaiementCotisation paiement = paiementService.addPaiement(request);
            return ResponseEntity.ok(paiement);
        } catch (AdherentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur serveur"));
        }
    }

    @GetMapping({"/adherent/{adherentId}"})
    public ResponseEntity<?> getAllPaiementsByAdherentId(@PathVariable String adherentId) {
        return ResponseEntity.ok(paiementService.getAllPaiementsByAdherentId(adherentId));
    }
}
