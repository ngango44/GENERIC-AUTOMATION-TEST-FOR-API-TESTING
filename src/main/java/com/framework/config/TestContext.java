package com.framework.config;

public class TestContext {
    private final int sheetIndex;
    private final String sheetName;
    private final String excelFilePath;
    private final TestConfiguration configuration;
    public TestContext(int sheetIndex, TestConfiguration configuration){
        this.sheetIndex = sheetIndex;
        this.configuration = configuration;
        this.sheetName = configuration.getSheetName(sheetIndex);
        this.excelFilePath = configuration.getExcelFileForSheet(sheetIndex);
    }
    public int getSheetIndex(){
        return  sheetIndex;
    }
    public String getSheetName(){
        return sheetName;
    }
    public String getExcelFilePath(){
        return excelFilePath;
    }
    public TestConfiguration getConfiguration(){
        return configuration;
    }
    public  boolean isLastSheet(){
        return  sheetIndex == configuration.getTotalSheets() -1;
    }
    @Override
    public String toString(){
        return "TestContext{" +
                "sheetIndex=" + sheetIndex +
                ", sheetName='" + sheetName + '\'' +
                ", excelFilePath='" + excelFilePath + '\'' +
                '}';
    }
}
