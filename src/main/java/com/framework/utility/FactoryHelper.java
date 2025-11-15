package com.framework.utility;

import com.framework.config.TestConfiguration;
import com.framework.constants.Constants.FactoryHelperConstants;
import com.framework.constants.Constants;
import com.framework.inmemorydatabase.InMemoryDatabaseHelper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FactoryHelper {
    /**
     * 1. Scan Excel file
     * 2. Đọc từng Excel file, tìm sheets có thể chạy
     * 3. Setup database và các utilities
     * 4.  Trả về kết quả
     */

    /**
     * Scan all Excel files in ExcelData folder
     * @return List of Excel file paths
     */
    public TestConfiguration buildConfiguration(){
        TestConfiguration.Builder configBuilder = new TestConfiguration.Builder();
        try {
            createTable();
            List<String> excelFiles = scanExcelFiles();
            for (String excelFile: excelFiles){
                configBuilder.addExcelFile(excelFile);
                List<SheetInfo> sheets = getRunnableSheets(excelFile);
                for (SheetInfo sheet: sheets){
                    configBuilder.addSheet(sheet.sheetName);
                    configBuilder.addSheetToExcelMapping(sheet.excelFilePath);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return configBuilder.build();
    }
    private List<String> scanExcelFiles(){
        final List<String> excelFiles = new ArrayList<>();
        try {
            File folder = new File(Constants.EXCEL_FOLDER_PATH);
            File[] files = folder.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".xlsx") && !name.startsWith("~"));
            if (files !=null && files.length > 0){
                Arrays.stream(files).forEach(file ->{
                    excelFiles.add(file.getAbsolutePath());
                });
            } else {
                System.out.println("No Excel files found in: " + Constants.EXCEL_FOLDER_PATH);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return excelFiles;
    }
    /**
     * Get runnable sheets from a specific Excel file
     * @param excelFilePath Path to Excel file
     * @return List of SheetInfo objects
     */
    private List<SheetInfo> getRunnableSheets(String excelFilePath) {
        List<SheetInfo> sheets = new ArrayList<>();
        try{
            ExcelReader excelread = new ExcelReader(excelFilePath);

            int totalSheets = excelread.getWorkbook().getNumberOfSheets();
            String sheetName = FactoryHelperConstants.SHEET_NAME.toString();

            // Check if sheet exists
            if (excelread.getWorkbook().getSheet(sheetName) == null) {
                return sheets;
            }

            int rows = excelread.getLastRowNumber(sheetName);

            for (int i = 1; i <= rows; i++) {
                String runmode = excelread.getCellData(sheetName, 1, i);
                String targetSheetName = excelread.getCellData(sheetName, 0, i);

                if (runmode.equalsIgnoreCase(Constants.DataUtilConstants.RUNMODE.toString())) {
                    sheets.add(new SheetInfo(targetSheetName, excelFilePath));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sheets;
    }
    private static class SheetInfo {
        final String sheetName;
        final String excelFilePath;

        SheetInfo(String sheetName, String excelFilePath) {
            this.sheetName = sheetName;
            this.excelFilePath = excelFilePath;
        }
    }
    private void createTable(){
        try{
            InMemoryDatabaseHelper inMemoryDatabaseHelper = new InMemoryDatabaseHelper();
            inMemoryDatabaseHelper.createTable();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
