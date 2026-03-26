package com.example.sen_scu.service.sen_csu.auth;

import com.example.sen_scu.dto.sen_csu.auth.AuthRequest;
import com.example.sen_scu.dto.sen_csu.auth.AuthResponse;
import com.example.sen_scu.dto.sen_csu.auth.RegisterRequest;
import com.example.sen_scu.model.sen_csu.Agent;
import com.example.sen_scu.repository.sen_csu.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AgentRepository agentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        if (agentRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        var agent = Agent.builder()
                .prenom(request.getPrenoms())
                .name(request.getNom())
                .email(request.getEmail())
                .telephone(request.getWhatsapp())
                .password(passwordEncoder.encode(request.getPassword()))
                .dateNaissance(request.getDateNaissance())
                .adresse(request.getAdresse())
                .sexe(request.getSexe())
                .region(request.getRegion())
                .departement(request.getDepartement())
                .commune(request.getCommune())
                .role(request.getRole())
                .build();

        agentRepository.save(agent);

        UserDetails userDetails = User.builder()
                .username(agent.getEmail())
                .password(agent.getPassword())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + agent.getRole())
                ))
                .build();

        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .user(convertToUserDto(agent))
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        var agent = agentRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .user(convertToUserDto(agent))
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        final String username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            var agent = agentRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            UserDetails userDetails = User.builder()
                    .username(agent.getEmail())
                    .password(agent.getPassword())
                    .authorities(Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + agent.getRole())
                    ))
                    .build();

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                var accessToken = jwtService.generateToken(userDetails);
                var newRefreshToken = jwtService.generateRefreshToken(userDetails);

                return AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(newRefreshToken)
                        .tokenType("Bearer")
                        .expiresIn(86400000L)
                        .user(convertToUserDto(agent))
                        .build();
            }
        }
        throw new RuntimeException("Token de rafraîchissement invalide");
    }

    private AuthResponse.UserDto convertToUserDto(Agent agent) {
        return AuthResponse.UserDto.builder()
                .id(agent.getId())
                .prenoms(agent.getPrenom())
                .nom(agent.getName())
                .email(agent.getEmail())
                .telephone(agent.getTelephone())
                .role(agent.getRole())
                .photo(agent.getPhoto())
                .build();
    }
}