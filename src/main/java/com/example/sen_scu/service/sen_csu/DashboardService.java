package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.repository.sen_csu.AdherentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AdherentRepository adherentRepository;

    private static final String[] MOIS_LABELS = {
            "Jan", "Fev", "Mar", "Avr", "Mai", "Juin",
            "Juil", "Aout", "Sep", "Oct", "Nov", "Dec"
    };

    /**
     * Returns all dashboard statistics for a given year.
     * If year is null, defaults to the current year.
     */
    public Map<String, Object> getDashboardStats(Integer year) {
        int targetYear = (year != null) ? year : LocalDate.now().getYear();

        Map<String, Object> result = new LinkedHashMap<>();

        // ── KPI totals ──
        long total = adherentRepository.countByYear(targetYear);
        long hommes = adherentRepository.countByYearAndSexe(targetYear, "Masculin")
                    + adherentRepository.countByYearAndSexe(targetYear, "Homme");
        long femmes = adherentRepository.countByYearAndSexe(targetYear, "Feminin")
                    + adherentRepository.countByYearAndSexe(targetYear, "Femme");
        long communes = adherentRepository.countDistinctCommunesByYear(targetYear);

        result.put("totalBeneficiaires", total);
        result.put("hommes", hommes);
        result.put("femmes", femmes);
        result.put("communesCouvertes", communes);
        result.put("year", targetYear);

        // ── Stats par departement ──
        List<Map<String, Object>> deptStats = new ArrayList<>();
        for (Object[] row : adherentRepository.statsByDeptAndYear(targetYear)) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("departement", row[0] != null ? row[0].toString() : "Inconnu");
            m.put("total", ((Number) row[1]).longValue());
            m.put("hommes", ((Number) row[2]).longValue());
            m.put("femmes", ((Number) row[3]).longValue());
            deptStats.add(m);
        }
        result.put("statsByDept", deptStats);

        // ── Stats par commune ──
        List<Map<String, Object>> communeStats = new ArrayList<>();
        for (Object[] row : adherentRepository.statsByCommuneAndYear(targetYear)) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("commune", row[0] != null ? row[0].toString() : "Inconnu");
            m.put("departement", row[1] != null ? row[1].toString() : "Inconnu");
            m.put("region", row[2] != null ? row[2].toString() : "Inconnu");
            m.put("total", ((Number) row[3]).longValue());
            m.put("hommes", ((Number) row[4]).longValue());
            m.put("femmes", ((Number) row[5]).longValue());
            communeStats.add(m);
        }
        result.put("statsByCommune", communeStats);

        // ── Stats par type de beneficiaire ──
        List<Map<String, Object>> typeStats = new ArrayList<>();
        for (Object[] row : adherentRepository.statsByTypeAndYear(targetYear)) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("type", row[0] != null ? row[0].toString() : "Inconnu");
            m.put("count", ((Number) row[1]).longValue());
            typeStats.add(m);
        }
        result.put("statsByType", typeStats);

        // ── Enrolement mensuel (rempli avec 0 pour les mois sans data) ──
        Map<Integer, Long> monthMap = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) monthMap.put(i, 0L);
        for (Object[] row : adherentRepository.enrollmentByMonthAndYear(targetYear)) {
            int month = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();
            monthMap.put(month, count);
        }
        List<Map<String, Object>> mensuelStats = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("mois", MOIS_LABELS[i - 1]);
            m.put("count", monthMap.get(i));
            mensuelStats.add(m);
        }
        result.put("enrolementMensuel", mensuelStats);

        // ── Available years ──
        List<Integer> years = adherentRepository.findDistinctYears();
        if (years.isEmpty()) {
            years = List.of(LocalDate.now().getYear());
        }
        result.put("availableYears", years);

        return result;
    }
}
