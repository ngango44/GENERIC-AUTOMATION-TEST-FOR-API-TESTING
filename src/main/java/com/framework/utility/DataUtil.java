package com.framework.utility;

import com.framework.config.TestContext;
import com.framework.config.TestContextProvider;
import com.framework.constants.Constants.DataUtilConstants;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

public class DataUtil {
    @DataProvider(name = "getData")
    public Object[][] getData(ITestContext testContext, Method method){
        Object[][] data = null;
        try {
            TestContext context = null;
            for (ITestNGMethod testNGMethod: testContext.getAllTestMethods()) {
                Object instance = testNGMethod.getInstance();
                if (instance instanceof TestContextProvider) {
                    context = ((TestContextProvider) instance).getContext();
                    break;
                }
            }
            if(context == null){
                System.err.println("ERROR: Could not get TestContext from test instance!");
                return new Object[0][0];
            }
            String excelFilePath = context.getExcelFilePath();
            String sheetName = context.getSheetName();
            ExcelReader excelReader = new ExcelReader(excelFilePath);
            int rows = excelReader.getLastRowNumber(sheetName);
            int cols = excelReader.getLastColNum(sheetName,1);
            int count = runModeCount(excelReader,sheetName,rows);
            data = new Object[count][1];
            LinkedHashMap<String, String> table = null;
            int value = 0;
            for (int i = 0; i<rows; i++){
                table = new LinkedHashMap<String, String>();
                if(excelReader.getCellData(sheetName,1,i).toLowerCase().trim()
                        .equalsIgnoreCase(DataUtilConstants.RUNMODE.toString())){
                    table = getTestCaseMap(excelReader,sheetName,i,cols,table);
                    table.put("baseUrl",excelReader.getCellData(sheetName, 1, 1)); // ← SỬA: row 1 thay vì row 0
                    data[value][0] = table;
                    value++;
                    while (excelReader.getCellData(sheetName,1,i+1).toLowerCase().trim().equals("")){
                        table = new LinkedHashMap<String, String>();
                        if(i+1 < rows){
                            i++;
                            table = getTestCaseMap(excelReader,sheetName,i,cols,table);
                            data[value][0] = table;
                            value++;
                        }else {
                            break;
                        }
                    }
                }
            }
            if (data == null) {
                return new Object[0][0];
            }

        }catch (Exception e){
            System.out.println("Exception in DataProvider: "+ e);
            e.printStackTrace();
            return new Object[0][0];
        }
        return data;
    }
    public LinkedHashMap<String, String> getTestCaseMap(ExcelReader excelReader, String sheetName, int row, int cols, LinkedHashMap<String, String> table){
        try{
            for (int j = 0; j < cols; j++){
                String headerValue = excelReader.getCellData(sheetName,j,2);
            }
            for (int j = 0; j < cols; j++){
                String headerValue = excelReader.getCellData(sheetName,j,1);
            }
            for (int j = 0; j < cols; j++){
                StringBuffer key = new StringBuffer(excelReader.getCellData(sheetName,j,2).toLowerCase().trim()); // ← SỬA: row 2 thay vì row 1
                StringBuffer value = new StringBuffer(excelReader.getCellData(sheetName,j,row).trim());
                table.put(key.toString().trim(), value.toString().trim());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return table;
    }
    public int runModeCount(ExcelReader excelReader, String sheetName, int rows){
        int count = 0;
        for (int i = 0; i <rows;i++){
            if(excelReader.getCellData(sheetName,1,i).toLowerCase().trim()
                    .equalsIgnoreCase(DataUtilConstants.RUNMODE.toString())){
                count++;
                while (excelReader.getCellData(sheetName,1,i+1).toLowerCase().trim().equals("")){
                    if(i+1 < rows){
                        i++;
                        count++;
                    }else {
                        break;
                    }
                }
            }
        }
        return count;
    }

}
