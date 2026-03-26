package com.example.sen_scu.utils;

import org.apache.poi.ss.usermodel.*;

public class ExcelUtils {

    private static final DataFormatter formatter = new DataFormatter();

    public static String getCellValue(Cell cell) {
        if (cell == null) return "";
        return formatter.formatCellValue(cell).trim();
    }
}

