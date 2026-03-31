package com.example.sen_scu.repository.sen_csu;

import com.example.sen_scu.model.sen_csu.Projet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Projet, String> {
    void deleteByAdherentId(String adherentId);
}
