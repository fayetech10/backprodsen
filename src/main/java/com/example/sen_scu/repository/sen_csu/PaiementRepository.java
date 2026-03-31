package com.example.sen_scu.repository.sen_csu;

import com.example.sen_scu.model.sen_csu.PaiementCotisation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaiementRepository extends MongoRepository<PaiementCotisation, String> {
    List<PaiementCotisation> findAllByAdherentId(String adherentId);
    boolean existsByReference(String reference);
    void deleteByAdherentId(String adherentId);
}
