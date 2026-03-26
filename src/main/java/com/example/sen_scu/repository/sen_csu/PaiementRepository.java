package com.example.sen_scu.repository.sen_csu;

import com.example.sen_scu.model.sen_csu.PaiementCotisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  PaiementRepository extends JpaRepository<PaiementCotisation, Long> {
    List<PaiementCotisation> findAllByAdherentId(Long adherentId);
    boolean existsByReference(String reference);
    void deleteByAdherentId(Long adherentId);
}
