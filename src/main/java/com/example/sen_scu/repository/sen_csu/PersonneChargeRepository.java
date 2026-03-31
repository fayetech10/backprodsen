package com.example.sen_scu.repository.sen_csu;


import com.example.sen_scu.model.sen_csu.PersonneCharge;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PersonneChargeRepository extends MongoRepository<PersonneCharge, String> {
    Optional<PersonneCharge> findByIdAndAdherentId(String id, String adherentId);

}
