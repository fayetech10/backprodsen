package com.example.sen_scu.utils;

import org.apache.poi.ss.usermodel.*;

public class ExcelStyleHelper {

    public static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        return headerStyle;
    }

    public static CellStyle createAdherentStyle(Workbook workbook) {
        CellStyle adherentStyle = workbook.createCellStyle();
        adherentStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        adherentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        adherentStyle.setBorderBottom(BorderStyle.THIN);
        adherentStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return adherentStyle;
    }

    public static CellStyle createPersonneChargeStyle(Workbook workbook) {
        CellStyle pcStyle = workbook.createCellStyle();
        pcStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        pcStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        pcStyle.setBorderBottom(BorderStyle.DOTTED);
        pcStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return pcStyle;
    }

    public static CellStyle createStandardStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return style;
    }
}
