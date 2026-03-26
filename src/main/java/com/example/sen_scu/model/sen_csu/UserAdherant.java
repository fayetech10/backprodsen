package com.example.sen_scu.model.sen_csu;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class UserAdherant {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String role = "ADHERENT";

    @OneToOne
    private Adherent adherent;
}
