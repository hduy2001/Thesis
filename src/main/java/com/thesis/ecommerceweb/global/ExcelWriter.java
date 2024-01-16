package com.thesis.ecommerceweb.global;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {
    public void appendRating(String username, int pid, double rating, long timestamp) {
        String filePath = "D:\\Thesis\\history\\ratings.xlsx";
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {

            Sheet sheet = workbook.getSheet("Sheet1");

            int lastRowNum = sheet.getLastRowNum();

            int rowIndex = lastRowNum + 1;
            Row dataRow = sheet.createRow(rowIndex);
            dataRow.createCell(0).setCellValue(username);
            dataRow.createCell(1).setCellValue(pid);
            dataRow.createCell(2).setCellValue(rating);
            dataRow.createCell(3).setCellValue(timestamp);

            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
