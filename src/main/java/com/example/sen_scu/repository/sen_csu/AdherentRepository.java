package com.example.sen_scu.repository.sen_csu;

import com.example.sen_scu.model.sen_csu.Adherent;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AdherentRepository extends MongoRepository<Adherent, String> {
    boolean existsByNumeroCNi(String numeroCNi);

    Adherent findAdherentByWhatsapp(String whatsapp);

    List<Adherent> findAllByAgentId(String agentId);

    boolean existsByClientUUID(String clientUUID);

    List<Adherent> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // ── Dashboard queries (MongoDB Aggregation) ──

    @Aggregation(pipeline = {
        "{ $match: { $expr: { $eq: [{ $year: '$createdAt' }, ?0] } } }"
    })
    List<Adherent> findAllByYear(int year);

    @Aggregation(pipeline = {
        "{ $group: { _id: { $year: '$createdAt' } } }",
        "{ $sort: { _id: -1 } }",
        "{ $project: { _id: 0, year: '$_id' } }"
    })
    List<Integer> findDistinctYears();

    @Aggregation(pipeline = {
        "{ $match: { $expr: { $eq: [{ $year: '$createdAt' }, ?0] } } }",
        "{ $count: 'total' }"
    })
    Long countByYear(int year);

    @Aggregation(pipeline = {
        "{ $match: { $expr: { $eq: [{ $year: '$createdAt' }, ?0] }, $or: [ { sexe: { $regex: ?1, $options: 'i' } } ] } }",
        "{ $count: 'total' }"
    })
    Long countByYearAndSexe(int year, String sexe);

    @Aggregation(pipeline = {
        "{ $match: { $expr: { $eq: [{ $year: '$createdAt' }, ?0] } } }",
        "{ $group: { " +
            "_id: '$departement', " +
            "total: { $sum: 1 }, " +
            "hommes: { $sum: { $cond: [{ $or: [{ $eq: [{ $toLower: '$sexe' }, 'masculin'] }, { $eq: [{ $toLower: '$sexe' }, 'homme'] }] }, 1, 0] } }, " +
            "femmes: { $sum: { $cond: [{ $or: [{ $eq: [{ $toLower: '$sexe' }, 'feminin'] }, { $eq: [{ $toLower: '$sexe' }, 'femme'] }] }, 1, 0] } } " +
        "} }",
        "{ $sort: { total: -1 } }",
        "{ $project: { _id: 0, departement: '$_id', total: 1, hommes: 1, femmes: 1 } }"
    })
    List<org.bson.Document> statsByDeptAndYear(int year);

    @Aggregation(pipeline = {
        "{ $match: { $expr: { $eq: [{ $year: '$createdAt' }, ?0] } } }",
        "{ $group: { " +
            "_id: { commune: '$commune', departement: '$departement', region: '$region' }, " +
            "total: { $sum: 1 }, " +
            "hommes: { $sum: { $cond: [{ $or: [{ $eq: [{ $toLower: '$sexe' }, 'masculin'] }, { $eq: [{ $toLower: '$sexe' }, 'homme'] }] }, 1, 0] } }, " +
            "femmes: { $sum: { $cond: [{ $or: [{ $eq: [{ $toLower: '$sexe' }, 'feminin'] }, { $eq: [{ $toLower: '$sexe' }, 'femme'] }] }, 1, 0] } } " +
        "} }",
        "{ $sort: { total: -1 } }",
        "{ $project: { _id: 0, commune: '$_id.commune', departement: '$_id.departement', region: '$_id.region', total: 1, hommes: 1, femmes: 1 } }"
    })
    List<org.bson.Document> statsByCommuneAndYear(int year);

    @Aggregation(pipeline = {
        "{ $match: { $expr: { $eq: [{ $year: '$createdAt' }, ?0] } } }",
        "{ $group: { _id: '$typeBenef', count: { $sum: 1 } } }",
        "{ $project: { _id: 0, type: '$_id', count: 1 } }"
    })
    List<org.bson.Document> statsByTypeAndYear(int year);

    @Aggregation(pipeline = {
        "{ $match: { $expr: { $eq: [{ $year: '$createdAt' }, ?0] } } }",
        "{ $group: { _id: { $month: '$createdAt' }, count: { $sum: 1 } } }",
        "{ $sort: { _id: 1 } }",
        "{ $project: { _id: 0, month: '$_id', count: 1 } }"
    })
    List<org.bson.Document> enrollmentByMonthAndYear(int year);

    @Aggregation(pipeline = {
        "{ $match: { $expr: { $eq: [{ $year: '$createdAt' }, ?0] } } }",
        "{ $group: { _id: '$commune' } }",
        "{ $count: 'total' }"
    })
    Long countDistinctCommunesByYear(int year);

}
