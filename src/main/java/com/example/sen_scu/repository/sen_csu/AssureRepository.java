package com.example.sen_scu.repository.sen_csu;

import com.example.sen_scu.model.sen_csu.Assure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssureRepository extends JpaRepository<Assure, Long> {
    List<Assure> findByCodeImmatriculation(String codeImmatriculation);

    Page<Assure> findByImportName(String importName, Pageable pageable);

    @Query("SELECT DISTINCT a.importName FROM Assure a WHERE a.importName IS NOT NULL")
    List<String> findDistinctImportNames();

    List<Assure> findByAgentCollect(String agentCollect);

    @Query("SELECT DISTINCT a.agentCollect FROM Assure a WHERE a.agentCollect IS NOT NULL AND a.agentCollect <> ''")
    List<String> findDistinctAgentCollects();
}
