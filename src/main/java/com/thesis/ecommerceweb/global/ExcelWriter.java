package com.thesis.ecommerceweb.global;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    public void appendOrders(String username, int pid, String name, int quantity, String invoiceDate, int unitPrice, String address) {
        String filePath = "D:\\Thesis\\history\\orders.xlsx";
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {

            Sheet sheet = workbook.getSheet("Sheet1");

            int lastRowNum = sheet.getLastRowNum();

            int rowIndex = lastRowNum + 1;
            Row dataRow = sheet.createRow(rowIndex);
            dataRow.createCell(0).setCellValue(username);
            dataRow.createCell(1).setCellValue(pid);
            dataRow.createCell(2).setCellValue(name);
            dataRow.createCell(3).setCellValue(quantity);
            dataRow.createCell(4).setCellValue(invoiceDate);
            dataRow.createCell(5).setCellValue(unitPrice);
            dataRow.createCell(6).setCellValue(address);

            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getTop10Pid() {
        String filePath = "D:\\Thesis\\history\\orders.xlsx";
        Map<Integer, Integer> pidQuantityMap = new HashMap<>();
        try {
            FileInputStream excelFile = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                int pid = (int) row.getCell(1).getNumericCellValue();
                int quantity = (int) row.getCell(3).getNumericCellValue();

                // Kiểm tra xem pid đã tồn tại trong map chưa
                if (pidQuantityMap.containsKey(pid)) {
                    // Nếu tồn tại, cộng thêm quantity vào giá trị hiện tại
                    int currentQuantity = pidQuantityMap.get(pid);
                    pidQuantityMap.put(pid, currentQuantity + quantity);
                } else {
                    // Nếu chưa tồn tại, thêm pid và quantity vào map
                    pidQuantityMap.put(pid, quantity);
                }
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sắp xếp map theo quantity giảm dần và lấy top 10
        List<Integer> top10PidList = pidQuantityMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return top10PidList;
    }

    public List<Integer> getOrdersMonthly() throws IOException, ParseException {
        // Đường dẫn đến file Excel
        String excelFilePath = "D:\\Thesis\\history\\orders.xlsx";

        // Load file Excel
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(inputStream);

        // Lấy sheet đầu tiên (có thể sửa đổi tùy theo file Excel của bạn)
        Sheet sheet = workbook.getSheetAt(0);

        // Tạo map để lưu trữ total price theo từng tháng
        Map<String, Integer> monthlyTotalPriceMap = new HashMap<>();

        // Format cho ngày từ file Excel
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Đọc dữ liệu từ sheet
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();

            // Skip header row
            if (currentRow.getRowNum() == 0) {
                continue;
            }

            // Lấy giá trị từ các cột tương ứng
            int quantity = (int) currentRow.getCell(3).getNumericCellValue();
            String invoiceDateStr = currentRow.getCell(4).getStringCellValue();
            int unitPrice = (int) currentRow.getCell(5).getNumericCellValue();


            // Tính total price
            int totalPrice = unitPrice * quantity;
            Date invoiceDate = dateFormat.parse(invoiceDateStr);

            // Kiểm tra năm của invoiceDate
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(invoiceDate);
            int year = calendar.get(Calendar.YEAR);

            // Nếu năm là 2023, xử lý dữ liệu
            if (year == 2023) {
                // Lấy tháng từ invoiceDate
                String monthKey = String.format("%d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);

                // Cập nhật total price cho tháng tương ứng trong map
                monthlyTotalPriceMap.put(monthKey, monthlyTotalPriceMap.getOrDefault(monthKey, 0) + totalPrice);
            }
        }

        // Đóng workbook và input stream
        workbook.close();
        inputStream.close();
        List<Integer> valueLists = new ArrayList<>();
        // Chuyển đổi map thành danh sách để sắp xếp
        List<Map.Entry<String, Integer>> sortedMonthlyTotalPriceList = new ArrayList<>(monthlyTotalPriceMap.entrySet());
        // Sắp xếp danh sách dựa trên khóa (tháng)
        Collections.sort(sortedMonthlyTotalPriceList, Map.Entry.comparingByKey());
        // In ra kết quả
        for (Map.Entry<String, Integer> entry : sortedMonthlyTotalPriceList) {
            valueLists.add(entry.getValue());
        }
        return valueLists;
    }

    public int getTotalIncomes(List<Integer> list) {
        int total = 0;
        for (int i = 0; i < 12; i++) {
            total += list.get(i);
        }
        return total;
    }

    public int countOrders() throws IOException, ParseException{
        String excelFilePath = "D:\\Thesis\\history\\orders.xlsx";
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(inputStream);

        // Chọn sheet (0 là sheet đầu tiên)
        Sheet sheet = workbook.getSheetAt(0);

        // Đếm số dòng (trừ header)
        int rowCount = sheet.getPhysicalNumberOfRows() - 1;

        // Đếm số cột (lấy số cột của dòng đầu tiên)
        int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();

        // Tính tổng số ô (số dòng * số cột)
        int totalCells = rowCount * columnCount;

        System.out.println("Total Cells (excluding header): " + totalCells);

        workbook.close();
        inputStream.close();
        return totalCells;
    }
}
