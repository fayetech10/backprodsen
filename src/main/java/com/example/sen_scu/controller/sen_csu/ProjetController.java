package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.dto.sen_csu.ProjetWithAdherentRequest;
import com.example.sen_scu.model.sen_csu.Projet;
import com.example.sen_scu.service.sen_csu.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/project")
public class ProjetController {

    private final ProjectService projectService;

    @PostMapping("/create-with-adherent")
    public ResponseEntity<?> createProjetWithAdherent(
            @RequestBody ProjetWithAdherentRequest request
    ) {
        try {
            Projet project = projectService.createProjetWithAdherent(request);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Projet et adhérent créés avec succès",
                            "data", Map.of(
                                    "projet_id", project.getId(),
                                    "adherent_id", project.getAdherent().getId()
                            )
                    )
            );

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "Le numéro CNI existe déjà."
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "Erreur lors de la création du projet",
                            "error", e.getMessage()
                    )
            );
        }
    }
    @GetMapping ("/all")
    public ResponseEntity<?> getAllProjet() {

        List<Projet> projets = projectService.getAllProjects();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Projets",
                "data", projets
        ));
    }
}
