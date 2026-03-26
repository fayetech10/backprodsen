package com.example.sen_scu.repository.sen_csu;

import com.example.sen_scu.model.sen_csu.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Projet, Long> {
    void deleteByAdherentId(Long adherentId);
}
