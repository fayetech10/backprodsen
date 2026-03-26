package com.example.sen_scu.repository.sen_csu;


import com.example.sen_scu.model.sen_csu.PersonneCharge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonneChargeRepository extends JpaRepository<PersonneCharge, Long> {
    Optional<PersonneCharge> findByIdAndAdherentId(Long id, Long adherentId);

}
