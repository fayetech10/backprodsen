package com.example.sen_scu.repository.sen_csu;

import com.example.sen_scu.model.sen_csu.Assure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AssureRepository extends MongoRepository<Assure, String> {
    List<Assure> findByCodeImmatriculation(String codeImmatriculation);

    Page<Assure> findByImportName(String importName, Pageable pageable);

    @Query(value = "{ 'importName': { $ne: null } }", fields = "{ 'importName': 1 }")
    List<Assure> findAllWithImportName();

    default List<String> findDistinctImportNames() {
        return findAllWithImportName().stream()
                .map(Assure::getImportName)
                .distinct()
                .toList();
    }

    List<Assure> findByAgentCollect(String agentCollect);

    @Query(value = "{ 'agentCollect': { $ne: null, $ne: '' } }", fields = "{ 'agentCollect': 1 }")
    List<Assure> findAllWithAgentCollect();

    default List<String> findDistinctAgentCollects() {
        return findAllWithAgentCollect().stream()
                .map(Assure::getAgentCollect)
                .distinct()
                .toList();
    }
}
