package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.model.sen_csu.Adherent;
import com.example.sen_scu.model.sen_csu.Assure;
import com.example.sen_scu.model.sen_csu.Beneficiaire;
import com.example.sen_scu.model.sen_csu.PersonneCharge;
import com.example.sen_scu.repository.sen_csu.AssureRepository;
import com.example.sen_scu.repository.sen_csu.BeneficiaireRepository;
import com.example.sen_scu.utils.ExcelStyleHelper;
import com.example.sen_scu.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.Normalizer;
import java.util.regex.Pattern;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {
    private final AssureRepository assureRepository;
    private final BeneficiaireRepository beneficiaireRepository;

    public ExcelService(AssureRepository assureRepository, BeneficiaireRepository beneficiaireRepository) {
        this.assureRepository = assureRepository;
        this.beneficiaireRepository = beneficiaireRepository;
    }

    public ByteArrayInputStream adherentsToExcel(List<Adherent> adherents) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100); // Window size 100

        // --- PRÉPARATION DES STYLES ---
        CellStyle headerStyle = ExcelStyleHelper.createHeaderStyle(workbook);
        CellStyle adherentStyle = ExcelStyleHelper.createAdherentStyle(workbook);
        CellStyle pcStyle = ExcelStyleHelper.createPersonneChargeStyle(workbook);

        // --- CRÉATION DES FEUILLES ---
        createAdherentSheet(workbook, "Liste SCU", adherents, headerStyle, adherentStyle, pcStyle);
        createAdherentSheet(workbook, "A IMPORTER", adherents, headerStyle, adherentStyle, pcStyle);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.dispose(); // Delete temp files

        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * Méthode pour remplir une feuille avec les données des adhérents
     */
    private void createAdherentSheet(SXSSFWorkbook workbook, String sheetName, List<Adherent> adherents,
            CellStyle headerStyle, CellStyle adhStyle, CellStyle pcStyle) {

        SXSSFSheet sheet = workbook.createSheet(sheetName);
        sheet.trackAllColumnsForAutoSizing();

        // Définition des colonnes (25 colonnes)
        String[] columns = {
                "TYPE", "ID", "NOM", "PRÉNOMS", "SEXE", "DATE NAISSANCE", "LIEU NAISSANCE",
                "SITUATION MATRIMONIALE", "WHATSAPP", "ADRESSE", "RÉGION", "DÉPARTEMENT",
                "COMMUNE", "RÉGIME", "TYPE BÉNÉF.", "TYPE ADHÉSION", "NUMÉRO CNI",
                "TYPE PIÈCE / EXTRAIT", "SECTEUR ACTIVITÉ", "LIEN PARENTÉ", "MONTANT TOTAL", "DATE CRÉATION",
                "PHOTO", "PHOTO RECTO", "PHOTO VERSO"
        };

        // Création de l'en-tête
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Figer la première ligne
        sheet.createFreezePane(0, 1);

        int rowIdx = 1;
        for (Adherent adh : adherents) {
            // 1. Lignes Personnes à Charge (Style Blanc) - AFFICHÉES EN PREMIER
            List<PersonneCharge> charges = adh.getPersonnesCharge();
            if (charges != null) {
                for (PersonneCharge pc : charges) {
                    Row rowPc = sheet.createRow(rowIdx++);
                    fillPersonneChargeRow(rowPc, pc, pcStyle);
                }
            }

            // 2. Ligne Adhérent (Style Bleu Ciel) - AFFICHÉE APRÈS SES CHARGES
            Row rowAdh = sheet.createRow(rowIdx++);
            fillAdherentRow(rowAdh, adh, adhStyle);
        }

        // Auto-ajustement des colonnes et Filtre automatique
        if (rowIdx > 1) {
            sheet.setAutoFilter(new CellRangeAddress(0, rowIdx - 1, 0, columns.length - 1));
        }
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void fillAdherentRow(Row row, Adherent adh, CellStyle style) {
        String[] values = {
                "ADHERENT",
                String.valueOf(adh.getId()),
                adh.getNom(),
                adh.getPrenoms(),
                adh.getSexe(),
                adh.getDateNaissance() != null ? adh.getDateNaissance().toString() : "",
                adh.getLieuNaissance(),
                adh.getSituationMatrimoniale(),
                adh.getWhatsapp(),
                adh.getAdresse(),
                adh.getRegion(),
                adh.getDepartement(),
                adh.getCommune(),
                adh.getRegime(),
                adh.getTypeBenef(),
                adh.getTypeAdhesion(),
                adh.getNumeroCNi(),
                adh.getTypePiece(),
                adh.getSecteurActivite(),
                "", // Pas de lien parenté pour l'adhérent lui-même
                adh.getMontantTotal() != null ? adh.getMontantTotal().toString() : "0.0",
                adh.getCreatedAt() != null ? adh.getCreatedAt().toString() : "",
                formatPhotoUrl(adh.getPhoto()),
                formatPhotoUrl(adh.getPhotoRecto()),
                formatPhotoUrl(adh.getPhotoVerso())
        };

        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(values[i] != null ? values[i] : "");
            cell.setCellStyle(style);
        }
    }

    private void fillPersonneChargeRow(Row row, PersonneCharge pc, CellStyle style) {
        String[] values = {
                "  ↳ CHARGE",
                pc.getId() != null ? pc.getId().toString() : "",
                pc.getNom(),
                pc.getPrenoms(),
                pc.getSexe(),
                pc.getDateNaissance() != null ? pc.getDateNaissance().toString() : "",
                pc.getLieuNaissance(),
                pc.getSituationM(), // Note: Adherent has situationMatrimoniale, PC has situationM
                pc.getWhatsapp(),
                pc.getAdresse(),
                pc.getAdherent() != null ? pc.getAdherent().getRegion() : "",
                pc.getAdherent() != null ? pc.getAdherent().getDepartement() : "",
                pc.getAdherent() != null ? pc.getAdherent().getCommune() : "",
                pc.getAdherent() != null ? pc.getAdherent().getRegime() : "",
                pc.getAdherent() != null ? pc.getAdherent().getTypeBenef() : "",
                pc.getAdherent() != null ? pc.getAdherent().getTypeAdhesion() : "",
                pc.getNumeroCNi(),
                pc.getNumeroExtrait(),
                "", // Pas de secteur d'activité pour les charges
                pc.getLienParent(),
                "", // Pas de montant total spécifique pour les charges
                pc.getCreatedAt() != null ? pc.getCreatedAt().toString() : "",
                formatPhotoUrl(pc.getPhoto()),
                formatPhotoUrl(pc.getPhotoRecto()),
                formatPhotoUrl(pc.getPhotoVerso())
        };

        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(values[i] != null ? values[i] : "");
            cell.setCellStyle(style);
        }
    }

    public void importExcel(MultipartFile file, String importName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Generate valid importName if not provided
            String finalImportName = (importName != null && !importName.isBlank())
                    ? importName
                    : "Import_" + System.currentTimeMillis();

            // Header-based column mapping
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Le fichier Excel est vide ou ne contient pas d'en-tête.");
            }

            Map<String, Integer> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                String header = normalize(ExcelUtils.getCellValue(cell));
                headerMap.put(header, cell.getColumnIndex());
            }

            System.out.println("[importExcel] Colonnes normalisées : " + headerMap.keySet());

            List<Assure> assures = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row))
                    continue;

                Assure assure = new Assure();
                assure.setDateEnregistrement(getCellValueSafe(row, headerMap, "date enregistrement"));
                assure.setCodeImmatriculation(getCellValueSafe(row, headerMap, "code immatriculation"));
                assure.setNoms(getCellValueSafe(row, headerMap, "nom"));
                assure.setPrenoms(getCellValueSafe(row, headerMap, "prenom"));
                assure.setDateNaissance(getCellValueSafe(row, headerMap, "date naissance"));
                assure.setSexe(getCellValueSafe(row, headerMap, "sexe"));
                assure.setTelephone(getCellValueSafe(row, headerMap, "telephone"));
                assure.setAdresse(getCellValueSafe(row, headerMap, "adresse"));
                assure.setRegime(getCellValueSafe(row, headerMap, "regime"));
                assure.setAssureur(getCellValueSafe(row, headerMap, "assureur"));
                assure.setTypeBenef(getCellValueSafe(row, headerMap, "type beneficiaire"));
                assure.setDateCotisation(getCellValueSafe(row, headerMap, "date cotisation"));
                assure.setDateFinCotisation(getCellValueSafe(row, headerMap, "date fin cotisation"));
                assure.setQrCodeUrl(getCellValueSafe(row, headerMap, "qr code url"));
                assure.setRegion(getCellValueSafe(row, headerMap, "region"));
                assure.setDepartement(getCellValueSafe(row, headerMap, "departement"));
                assure.setCommune(getCellValueSafe(row, headerMap, "commune"));
                assure.setGroupe(getCellValueSafe(row, headerMap, "groupe"));
                assure.setTypeAdhesion(getCellValueSafe(row, headerMap, "type_adhesion"));
                assure.setTypeCotisation(getCellValueSafe(row, headerMap, "type_cotisation"));
                assure.setCni(getCellValueSafe(row, headerMap, "cni"));
                assure.setPhoto(getCellValueSafe(row, headerMap, "photo"));
                assure.setAgentCollect(getCellValueSafe(row, headerMap, "agent"));
                assure.setImportName(finalImportName);

                assures.add(assure);
            }

            if (!assures.isEmpty()) {
                assureRepository.saveAll(assures);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'importation Excel : " + e.getMessage(), e);
        }
    }

    public void importExcelBenef(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // --- Lecture de l'en-tête pour créer le mapping colonne -> index ---
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Le fichier Excel est vide ou ne contient pas d'en-tête.");
            }

            Map<String, Integer> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                String header = normalize(ExcelUtils.getCellValue(cell));
                headerMap.put(header, cell.getColumnIndex());
            }

            System.out.println("[importExcelBenef] Colonnes normalisées : " + headerMap.keySet());

            List<Beneficiaire> beneficiaries = new ArrayList<>();

            // --- Parcours des lignes (à partir de 1 pour ignorer l'en-tête) ---
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row))
                    continue;

                Beneficiaire beneficiaire = new Beneficiaire();

                // Récupération sécurisée des valeurs par nom de colonne
                beneficiaire.setAgentCollect(getCellValueSafe(row, headerMap, "agent"));
                beneficiaire.setAdresse(getCellValueSafe(row, headerMap, "ADRESSE"));
                beneficiaire.setAssureur(getCellValueSafe(row, headerMap, "assureur"));
                beneficiaire.setBeneficiaire(getCellValueSafe(row, headerMap, "beneficiaire"));
                beneficiaire.setCommune(getCellValueSafe(row, headerMap, "Commune"));
                beneficiaire.setDateNaissance(getCellValueSafe(row, headerMap, "DATE DE NAISSANCE"));
                beneficiaire.setDepartement(getCellValueSafe(row, headerMap, "Département"));
                beneficiaire.setRegion(getCellValueSafe(row, headerMap, "Région "));
                beneficiaire.setRegime(getCellValueSafe(row, headerMap, "Regime"));
                beneficiaire.setDate(getCellValueSafe(row, headerMap, "submission_time"));


                beneficiaire.setLieuNaissance(getCellValueSafe(row, headerMap, "LIEU DE NAISSANCE"));
                beneficiaire.setNom(getCellValueSafe(row, headerMap, "NOM"));
                beneficiaire.setPrenoms(getCellValueSafe(row, headerMap, "PRENOMS"));
                beneficiaire.setSexe(getCellValueSafe(row, headerMap, "sexe"));
                beneficiaire.setSituationM(getCellValueSafe(row, headerMap, "SITUATION MATRIMONIALE"));
                beneficiaire.setTypeBenef(getCellValueSafe(row, headerMap, "Type de bénéficiaires"));
                beneficiaire.setTypeAdhesion(getCellValueSafe(row, headerMap, "Type d'adhésion"));

                beneficiaries.add(beneficiaire);
            }

            // Sauvegarde en batch pour plus de performance
            if (!beneficiaries.isEmpty()) {
                beneficiaireRepository.saveAll(beneficiaries);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'importation Excel : " + e.getMessage(), e);
        }
    }

    /**
     * Récupère la valeur d'une cellule de manière sécurisée à partir du nom de
     * colonne.
     */
    private static String normalize(String s) {
        if (s == null)
            return "";
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase().trim();
    }

    private String getCellValueSafe(Row row, Map<String, Integer> headerMap, String columnName) {
        String key = normalize(columnName);
        Integer colIndex = headerMap.get(key);
        if (colIndex == null)
            return null; // colonne absente
        Cell cell = row.getCell(colIndex);
        return ExcelUtils.getCellValue(cell);
    }

    /**
     * Vérifie si la ligne Excel est vide (toutes les cellules nulles ou vides)
     */
    private boolean isRowEmpty(Row row) {
        for (Cell cell : row) {
            if (cell != null && !ExcelUtils.getCellValue(cell).isBlank()) {
                return false;
            }
        }
        return true;
    }

    public ByteArrayInputStream assuresToExcel(List<Assure> assures) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        CellStyle headerStyle = ExcelStyleHelper.createHeaderStyle(workbook);
        CellStyle standardStyle = ExcelStyleHelper.createStandardStyle(workbook);

        SXSSFSheet sheet = workbook.createSheet("Liste Assurés");
        sheet.trackAllColumnsForAutoSizing();

        String[] columns = {
                "DATE ENREGISTREMENT", "CODE IMMATRICULATION", "NOM", "PRÉNOM",
                "DATE NAISSANCE", "SEXE", "TÉLÉPHONE", "ADRESSE", "RÉGIME", "ASSUREUR",
                "TYPE BÉNÉFICIAIRE", "DATE COTISATION", "DATE FIN COTISATION",
                "QR CODE URL", "RÉGION", "DÉPARTEMENT", "COMMUNE", "GROUPE",
                "TYPE ADHÉSION", "TYPE COTISATION", "CNI", "PHOTO",
                "CARTE ASSURÉ", "DATE REMISE"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        sheet.createFreezePane(0, 1);

        int rowIdx = 1;
        for (Assure assure : assures) {
            Row row = sheet.createRow(rowIdx++);
            String[] values = {
                    assure.getDateEnregistrement(), assure.getCodeImmatriculation(),
                    assure.getNoms(), assure.getPrenoms(),
                    assure.getDateNaissance(), assure.getSexe(),
                    assure.getTelephone(), assure.getAdresse(),
                    assure.getRegime(), assure.getAssureur(),
                    assure.getTypeBenef(), assure.getDateCotisation(),
                    assure.getDateFinCotisation(), assure.getQrCodeUrl(),
                    assure.getRegion(), assure.getDepartement(),
                    assure.getCommune(), assure.getGroupe(),
                    assure.getTypeAdhesion(), assure.getTypeCotisation(),
                    assure.getCni(), assure.getPhoto(),
                    assure.getCarteAssure(), assure.getDateRemise()
            };

            for (int i = 0; i < values.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(values[i] != null ? values[i] : "");
                cell.setCellStyle(standardStyle);
            }
        }

        if (rowIdx > 1) {
            sheet.setAutoFilter(new CellRangeAddress(0, rowIdx - 1, 0, columns.length - 1));
        }
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.dispose();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public Page<Assure> getAssures(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return assureRepository.findAll(pageable);
    }

    public Page<Assure> getAssuresByImportName(String importName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return assureRepository.findByImportName(importName, pageable);
    }

    public List<String> getDistinctImportNames() {
        return assureRepository.findDistinctImportNames();
    }

    public List<String> getDistinctAgents() {
        return assureRepository.findDistinctAgentCollects();
    }

    public int bulkValidateByAgent(String agentCollect) {
        List<Assure> assures = assureRepository.findByAgentCollect(agentCollect);
        String today = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        int count = 0;
        for (Assure assure : assures) {
            if (assure.getCarteAssure() == null || assure.getCarteAssure().isBlank()) {
                assure.setCarteAssure("OUI");
                assure.setDateRemise(today);
                assureRepository.save(assure);
                count++;
            }
        }
        return count;
    }

    public Assure updateCarteStatus(String id, String carteAssure, String dateRemise) {
        Assure assure = assureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assuré non trouvé avec l'id : " + id));
        assure.setCarteAssure(carteAssure);
        assure.setDateRemise(dateRemise);
        return assureRepository.save(assure);
    }

    /**
     * Import carte list from Excel.
     * Expected columns: "Code Immatriculation", "Carte Assure", "Date Remise".
     * Matches existing assurés by codeImmatriculation and updates their carte
     * status.
     */
    public Map<String, Object> importCartes(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Le fichier Excel est vide ou ne contient pas d'en-tête.");
            }

            Map<String, Integer> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                String header = normalize(ExcelUtils.getCellValue(cell));
                headerMap.put(header, cell.getColumnIndex());
            }

            System.out.println("[importCartes] Colonnes normalisées : " + headerMap.keySet());

            int updated = 0;
            int notFound = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row))
                    continue;

                String code = getCellValueSafe(row, headerMap, "code immatriculation");
                String carteAssure = getCellValueSafe(row, headerMap, "carte assure");
                String dateRemise = getCellValueSafe(row, headerMap, "date remise");

                if (code == null || code.isBlank())
                    continue;

                List<Assure> matches = assureRepository.findByCodeImmatriculation(code.trim());
                if (!matches.isEmpty()) {
                    for (Assure assure : matches) {
                        if (carteAssure != null && !carteAssure.isBlank()) {
                            assure.setCarteAssure(carteAssure.trim());
                        }
                        if (dateRemise != null && !dateRemise.isBlank()) {
                            assure.setDateRemise(dateRemise.trim());
                        }
                        assureRepository.save(assure);
                        updated++;
                    }
                } else {
                    notFound++;
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("updated", updated);
            result.put("notFound", notFound);
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'importation des cartes : " + e.getMessage(), e);
        }
    }

    public Page<Beneficiaire> getBenef(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return beneficiaireRepository.findAll(pageable);
    }

    private String formatPhotoUrl(String filename) {
        if (filename == null || filename.isEmpty())
            return "";
        // Retourne l'URL complète pour accéder à la photo via le controller
        return "/api/photos/" + filename;
    }
}