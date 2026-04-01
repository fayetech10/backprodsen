package com.example.sen_scu.controller.sen_csu;

import com.example.sen_scu.dto.sen_csu.*;
import com.example.sen_scu.dto.sen_csu.auth.AuthRequest;
import com.example.sen_scu.dto.sen_csu.auth.AuthResponse;
import com.example.sen_scu.model.sen_csu.Agent;
import com.example.sen_scu.service.sen_csu.AgentService;
import com.example.sen_scu.service.sen_csu.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agent")
@Tag(name = "Agent", description = "Gestion des agents")
@Slf4j
public class AgentController {

    private final AgentService agentService;
    private final AuthService authService;

    // ==================== AUTHENTIFICATION ====================

    @Operation(summary = "Connexion agent", description = "Authentifie un agent et retourne un token JWT")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AgentLoginRequest request) {
        try {
            // Déléguer au service d'authentification JWT
            AuthRequest authRequest = AuthRequest.builder()
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .build();

            AuthResponse authResponse = authService.authenticate(authRequest);

            log.info("Connexion réussie pour l'agent: {}", request.getEmail());

            return ResponseEntity.ok(ApiResponse.success("Connexion réussie", authResponse));

        } catch (BadCredentialsException e) {
            log.warn("Tentative de connexion échouée pour: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Email ou mot de passe incorrect"));

        } catch (Exception e) {
            log.error("Erreur lors de la connexion pour: {}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur interne lors de la connexion"));
        }
    }

    // ==================== CRUD ====================

    @Operation(summary = "Créer un agent", description = "Crée un nouveau compte agent")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody AgentCreateRequest request) {
        try {
            Agent saved = agentService.create(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Agent créé avec succès", AgentResponse.fromEntity(saved)));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(summary = "Lister tous les agents", description = "Récupère la liste de tous les agents")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<AgentResponse> agents = agentService.getAll().stream()
                .map(AgentResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(
                "Liste des agents récupérée avec succès", agents));
    }

    @Operation(summary = "Récupérer un agent par ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return agentService.findById(id)
                .map(agent -> ResponseEntity.ok(
                        ApiResponse.success("Agent trouvé", AgentResponse.fromEntity(agent))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Agent non trouvé avec l'ID: " + id)));
    }

    @Operation(summary = "Récupérer un agent par email")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByEmail(@PathVariable String email) {
        return agentService.findByEmail(email)
                .map(agent -> ResponseEntity.ok(
                        ApiResponse.success("Agent trouvé", AgentResponse.fromEntity(agent))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Agent non trouvé avec l'email: " + email)));
    }

    @Operation(summary = "Mettre à jour un agent")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id,
                                    @Valid @RequestBody AgentUpdateRequest request) {
        try {
            Agent updated = agentService.update(id, request);

            return ResponseEntity.ok(ApiResponse.success(
                    "Agent mis à jour avec succès", AgentResponse.fromEntity(updated)));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(summary = "Supprimer un agent")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            agentService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Agent supprimé avec succès"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // ==================== GESTION MOT DE PASSE ====================

    @Operation(summary = "Changer le mot de passe", description = "Permet à un agent de changer son mot de passe")
    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable String id,
                                            @Valid @RequestBody ChangePasswordRequest request) {
        try {
            agentService.changePassword(id, request);
            return ResponseEntity.ok(ApiResponse.success("Mot de passe modifié avec succès"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(summary = "Réinitialiser le mot de passe", description = "Réinitialise le mot de passe d'un agent (admin)")
    @PutMapping("/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable String id) {
        try {
            agentService.resetPassword(id, null);
            return ResponseEntity.ok(ApiResponse.success("Mot de passe réinitialisé avec succès"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // ==================== STATISTIQUES ====================

    @Operation(summary = "Nombre total d'agents")
    @GetMapping("/count")
    public ResponseEntity<?> count() {
        long total = agentService.count();
        return ResponseEntity.ok(ApiResponse.success("Nombre d'agents", total));
    }
}
