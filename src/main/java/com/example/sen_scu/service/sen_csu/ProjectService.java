package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.ProjetWithAdherentRequest;
import com.example.sen_scu.model.sen_csu.Projet;

import java.util.List;

public interface ProjectService {
            Projet createProjetWithAdherent(ProjetWithAdherentRequest request);
            List<Projet> getAllProjects();
}
