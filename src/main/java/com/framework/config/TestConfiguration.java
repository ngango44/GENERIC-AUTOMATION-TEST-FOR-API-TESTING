package com.framework.config;

import java.util.ArrayList;
import java.util.List;

public class TestConfiguration {
    private final String baseUrl;
    private final List<String> sheetNames;
    private final List<String> excelFiles;
    private final List<String> sheetToExcelMapping;
    private TestConfiguration (Builder builder){
        this.baseUrl = builder.baseUrl;
        this.sheetNames = new ArrayList<>(builder.sheetNames);
        this.excelFiles = new ArrayList<>(builder.excelFiles);
        this.sheetToExcelMapping = new ArrayList<>(builder.sheetToExcelMapping);
    }
    public String getBaseUrl(){
        return  baseUrl;
    }
    public int getTotalSheets() {
        return sheetNames.size();
    }
    public String getSheetName(int index){
        if(index >= 0 && index < sheetNames.size()){
            return sheetNames.get(index);
        }
        throw new IndexOutOfBoundsException("Sheet index out of bounds: "+ index);
    }
    public String getExcelFileForSheet(int index){
        if(index >= 0 && index < sheetToExcelMapping.size()){
            return sheetToExcelMapping.get(index);
        }
        throw new IndexOutOfBoundsException("Sheet index out of bounds: "+index);
    }

    public static class Builder{
        private String baseUrl = "";
        private List<String> sheetNames = new ArrayList<>();
        private List<String> excelFiles = new ArrayList<>();
        private List<String> sheetToExcelMapping = new ArrayList<>();
        public Builder baseUrl(String baseUrl){
            this.baseUrl = baseUrl;
            return this;
        }
        public Builder addSheet(String sheetName){
            this.sheetNames.add(sheetName);
            return this;
        }
        public Builder addExcelFile(String excelFile){
            this.excelFiles.add(excelFile);
            return this;
        }
        public Builder addSheetToExcelMapping(String mapping){
            this.sheetToExcelMapping.add(mapping);
            return this;
        }
        public Builder sheetNames(List<String> sheetNames) {
            this.sheetNames = new ArrayList<>(sheetNames);
            return this;
        }

        public Builder excelFiles(List<String> excelFiles) {
            this.excelFiles = new ArrayList<>(excelFiles);
            return this;
        }

        public Builder sheetToExcelMapping(List<String> mapping) {
            this.sheetToExcelMapping = new ArrayList<>(mapping);
            return this;
        }
        public TestConfiguration build(){
            return new TestConfiguration(this);
        }
    }
    @Override
    public String toString(){
        return "TestConfiguration{" +
                "baseUrl='" + baseUrl + '\'' +
                ", sheetNames=" + sheetNames +
                ", excelFiles=" + excelFiles +
                ", totalSheets=" +sheetNames.size() +
                '}';
    }
}
