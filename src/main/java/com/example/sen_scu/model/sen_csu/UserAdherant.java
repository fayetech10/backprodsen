package com.example.sen_scu.model.sen_csu;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userAdherants")
@Data
public class UserAdherant {
    @Id
    private String id;

    private String username;
    private String password;
    private String role = "ADHERENT";

    @DBRef
    private Adherent adherent;
}
