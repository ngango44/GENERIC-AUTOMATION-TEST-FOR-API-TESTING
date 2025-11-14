package com.framework.utility;

import com.framework.constants.Constants.DataUtilConstants;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

public class ExcelReader {
    private FileInputStream fis;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private XSSFRow row;
    private XSSFCell cell;
    public ExcelReader(String filePath){
        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getCellData(String sheetName, String columName, int rowNum){
        try {
            String cellVal = "";
            sheet = workbook.getSheet(sheetName);
            cellVal = nullToEmpty(sheet);
            row = sheet.getRow(0);
            int col_Num = -1;
            for (int i = 0;i<row.getLastCellNum();i++){
                if(row.getCell(i).getStringCellValue().trim().equals(columName.trim()))
                    col_Num = i;
            }
            if (col_Num == -1)
                cellVal = "";
            row = sheet.getRow(rowNum -1);
            cellVal = nullToEmpty(row);
            cell = row.getCell(col_Num);
            cellVal = nullToEmpty(cell);
            if (cell.getCellType() == CellType.STRING)
                cellVal = cell.getStringCellValue();
            else if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
                cellVal = cell.getStringCellValue().trim();
            } else if (cell.getCellType() == CellType.BLANK) {
                cellVal = "";
            }else
                cellVal = String.valueOf(cell.getBooleanCellValue());
            return cellVal;
        }catch (Exception e){
            e.printStackTrace();
            return "row " + rowNum + "or colum " + columName + "does not exist in sheet " + sheetName;
        }
    }
    public String getCellData(String sheetName, int colNum, int rowNum){
        try {
            String cellVal = "";
            sheet = workbook.getSheet(sheetName);
            cellVal = nullToEmpty(sheet);

            row = sheet.getRow(rowNum -1);
            cellVal = nullToEmpty(row);
            cell = row.getCell(colNum);
            cellVal = nullToEmpty(cell);
            if (cell.getCellType() == CellType.STRING)
                cellVal = cell.getStringCellValue();
            else if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
                cellVal = cell.getStringCellValue().trim();
            } else if (cell.getCellType() == CellType.BLANK) {
                cellVal = "";
            }else
                cellVal = String.valueOf(cell.getBooleanCellValue());
            return cellVal;
        }catch (Exception e){
            e.printStackTrace();
            return "row " + rowNum + "or colum " + colNum + "does not exist in sheet " + sheetName;
        }
    }
    private <T> String nullToEmpty(T var) {
        return var == null ? "" : var.toString();
    }
    public int getLastRowNumber(String sheetName){
        int rows = 0;
        while (!getCellData(sheetName,0,rows).equals("")){
            rows++;
        }
        return rows;
    }
    public int getLastColNum(String sheetName, int number){
        int index = workbook.getSheetIndex(sheetName);
        if(index == -1)
            return 0;
        Sheet sheet = workbook.getSheetAt(index);
        return sheet.getRow(number).getLastCellNum();
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }
}
