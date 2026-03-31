package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.PersonneChargeRequest;
import com.example.sen_scu.model.sen_csu.PersonneCharge;

public interface PersonneEnchargeSevice {
    PersonneCharge savePersonneChargeRequest(PersonneChargeRequest request, String adherentId);
    PersonneCharge update(String adherentId, String pcId, PersonneChargeRequest request);

    void delete(String adherentId, String pcId);
}
