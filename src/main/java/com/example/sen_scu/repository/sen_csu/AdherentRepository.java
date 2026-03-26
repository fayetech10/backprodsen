package com.example.sen_scu.repository.sen_csu;

import com.example.sen_scu.model.sen_csu.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdherentRepository extends JpaRepository<Adherent, Long> {
    boolean existsBynumeroCNi(String numeroCNi);

    Adherent findAdherentByWhatsapp(String whatsapp);

    List<Adherent> findAllByAgentId(Long agentId);

    boolean existsByClientUUID(String clientUUID);

    List<Adherent> findAllByCreatedAtBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);

    // ── Dashboard queries ──

    @Query("SELECT a FROM Adherent a WHERE YEAR(a.createdAt) = :year")
    List<Adherent> findAllByYear(@Param("year") int year);

    @Query("SELECT DISTINCT YEAR(a.createdAt) FROM Adherent a ORDER BY YEAR(a.createdAt) DESC")
    List<Integer> findDistinctYears();

    @Query("SELECT COUNT(a) FROM Adherent a WHERE YEAR(a.createdAt) = :year")
    long countByYear(@Param("year") int year);

    @Query("SELECT COUNT(a) FROM Adherent a WHERE YEAR(a.createdAt) = :year AND LOWER(a.sexe) = LOWER(:sexe)")
    long countByYearAndSexe(@Param("year") int year, @Param("sexe") String sexe);

    @Query("SELECT a.departement, COUNT(a), " +
           "SUM(CASE WHEN LOWER(a.sexe) = 'masculin' OR LOWER(a.sexe) = 'homme' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN LOWER(a.sexe) = 'feminin' OR LOWER(a.sexe) = 'femme' THEN 1 ELSE 0 END) " +
           "FROM Adherent a WHERE YEAR(a.createdAt) = :year GROUP BY a.departement ORDER BY COUNT(a) DESC")
    List<Object[]> statsByDeptAndYear(@Param("year") int year);

    @Query("SELECT a.commune, a.departement, a.region, COUNT(a), " +
           "SUM(CASE WHEN LOWER(a.sexe) = 'masculin' OR LOWER(a.sexe) = 'homme' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN LOWER(a.sexe) = 'feminin' OR LOWER(a.sexe) = 'femme' THEN 1 ELSE 0 END) " +
           "FROM Adherent a WHERE YEAR(a.createdAt) = :year GROUP BY a.commune, a.departement, a.region ORDER BY COUNT(a) DESC")
    List<Object[]> statsByCommuneAndYear(@Param("year") int year);

    @Query("SELECT a.typeBenef, COUNT(a) FROM Adherent a WHERE YEAR(a.createdAt) = :year GROUP BY a.typeBenef")
    List<Object[]> statsByTypeAndYear(@Param("year") int year);

    @Query("SELECT MONTH(a.createdAt), COUNT(a) FROM Adherent a WHERE YEAR(a.createdAt) = :year GROUP BY MONTH(a.createdAt) ORDER BY MONTH(a.createdAt)")
    List<Object[]> enrollmentByMonthAndYear(@Param("year") int year);

    @Query("SELECT COUNT(DISTINCT a.commune) FROM Adherent a WHERE YEAR(a.createdAt) = :year")
    long countDistinctCommunesByYear(@Param("year") int year);

}
