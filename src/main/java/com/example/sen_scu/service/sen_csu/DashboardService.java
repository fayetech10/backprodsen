package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.repository.sen_csu.AdherentRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "dashboardStats", key = "#year != null ? #year : T(java.time.LocalDate).now().getYear()")
    public Map<String, Object> getDashboardStats(Integer year) {
        int targetYear = (year != null) ? year : LocalDate.now().getYear();

        Map<String, Object> result = new LinkedHashMap<>();

        // ── KPI totals ──
        Long totalRaw = adherentRepository.countByYear(targetYear);
        long total = (totalRaw != null) ? totalRaw : 0L;

        Long hommesM = adherentRepository.countByYearAndSexe(targetYear, "Masculin");
        Long hommesH = adherentRepository.countByYearAndSexe(targetYear, "Homme");
        long hommes = ((hommesM != null) ? hommesM : 0L) + ((hommesH != null) ? hommesH : 0L);

        Long femmesF = adherentRepository.countByYearAndSexe(targetYear, "Feminin");
        Long femmesFe = adherentRepository.countByYearAndSexe(targetYear, "Femme");
        long femmes = ((femmesF != null) ? femmesF : 0L) + ((femmesFe != null) ? femmesFe : 0L);

        Long communesRaw = adherentRepository.countDistinctCommunesByYear(targetYear);
        long communes = (communesRaw != null) ? communesRaw : 0L;

        result.put("totalBeneficiaires", total);
        result.put("hommes", hommes);
        result.put("femmes", femmes);
        result.put("communesCouvertes", communes);
        result.put("year", targetYear);

        // ── Stats par departement ──
        List<Map<String, Object>> deptStats = new ArrayList<>();
        List<Document> deptDocs = adherentRepository.statsByDeptAndYear(targetYear);
        if (deptDocs != null) {
            for (Document doc : deptDocs) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("departement", doc.get("departement") != null ? doc.get("departement").toString() : "Inconnu");
                m.put("total", getNumberValue(doc, "total"));
                m.put("hommes", getNumberValue(doc, "hommes"));
                m.put("femmes", getNumberValue(doc, "femmes"));
                deptStats.add(m);
            }
        }
        result.put("statsByDept", deptStats);

        // ── Stats par commune ──
        List<Map<String, Object>> communeStats = new ArrayList<>();
        List<Document> communeDocs = adherentRepository.statsByCommuneAndYear(targetYear);
        if (communeDocs != null) {
            for (Document doc : communeDocs) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("commune", doc.get("commune") != null ? doc.get("commune").toString() : "Inconnu");
                m.put("departement", doc.get("departement") != null ? doc.get("departement").toString() : "Inconnu");
                m.put("region", doc.get("region") != null ? doc.get("region").toString() : "Inconnu");
                m.put("total", getNumberValue(doc, "total"));
                m.put("hommes", getNumberValue(doc, "hommes"));
                m.put("femmes", getNumberValue(doc, "femmes"));
                communeStats.add(m);
            }
        }
        result.put("statsByCommune", communeStats);

        // ── Stats par type de beneficiaire ──
        List<Map<String, Object>> typeStats = new ArrayList<>();
        List<Document> typeDocs = adherentRepository.statsByTypeAndYear(targetYear);
        if (typeDocs != null) {
            for (Document doc : typeDocs) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("type", doc.get("type") != null ? doc.get("type").toString() : "Inconnu");
                m.put("count", getNumberValue(doc, "count"));
                typeStats.add(m);
            }
        }
        result.put("statsByType", typeStats);

        // ── Enrolement mensuel (rempli avec 0 pour les mois sans data) ──
        Map<Integer, Long> monthMap = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) monthMap.put(i, 0L);
        List<Document> monthDocs = adherentRepository.enrollmentByMonthAndYear(targetYear);
        if (monthDocs != null) {
            for (Document doc : monthDocs) {
                int month = getNumberValue(doc, "month").intValue();
                long count = getNumberValue(doc, "count");
                monthMap.put(month, count);
            }
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
        if (years == null || years.isEmpty()) {
            years = List.of(LocalDate.now().getYear());
        }
        result.put("availableYears", years);

        return result;
    }

    private Long getNumberValue(Document doc, String key) {
        Object val = doc.get(key);
        if (val instanceof Number) {
            return ((Number) val).longValue();
        }
        return 0L;
    }
}
