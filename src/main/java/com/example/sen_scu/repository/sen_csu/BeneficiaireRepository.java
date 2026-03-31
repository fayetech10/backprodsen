package com.example.sen_scu.repository.sen_csu;

import com.example.sen_scu.model.sen_csu.Beneficiaire;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BeneficiaireRepository extends MongoRepository<Beneficiaire, String> {
}
