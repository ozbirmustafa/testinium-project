package com.beymen.utils;

import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {

    private static final Logger logger = LoggerHelper.getLogger(ExcelReader.class);

    private final Workbook workbook;

    public ExcelReader(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            this.workbook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", e.getMessage());
            throw new RuntimeException();
        }
    }

    public String getCellData(String sheetName, int rowNum, int colNum) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            logger.error("Sheet not found: {}", sheetName);
            return null;
        }
        Row row = sheet.getRow(rowNum - 1); // Row index is 0-based
        if (row == null) {
            logger.error("Row not found: {}", rowNum);
            return null;
        }
        Cell cell = row.getCell(colNum - 1); // Column index is 0-based
        if (cell == null) {
            logger.error("Cell not found: {}", colNum);
            return null;
        }

        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            logger.error("Error closing Excel workbook: {}", e.getMessage());
        }
    }
}