package com.framework.utility;


import com.framework.constants.Constants.ExcelColumnNameConstant;
import com.framework.inmemorydatabase.InMemoryDatabaseHelper;
import io.restassured.response.Response;
import org.apache.http.util.TextUtils;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractDynamicData {
    InMemoryDatabaseHelper inMemoryDatabaseHelper = new InMemoryDatabaseHelper();
    public void extractDynamicRequestValue(LinkedHashMap<String, String> data){
        extractDynamicValues(ExcelColumnNameConstant.TEST_URL.toString(),data);
        extractDynamicValues(ExcelColumnNameConstant.TEST_INPUT_JSON.toString(),data);
        extractDynamicValues(ExcelColumnNameConstant.TEST_HEADERS.toString(),data);
        extractDynamicValues(ExcelColumnNameConstant.TEST_PARAMETERS.toString(),data);
        extractDynamicValues(ExcelColumnNameConstant.TEST_METHOD_AND_JSON_PATH.toString(),data);
    }
    private void extractDynamicValues(String value, LinkedHashMap<String, String> data){
        String dataValue = data.get(value);
        if(dataValue != null) {
            if (dataValue.contains("#")) {
                extractSingleDynamicValue(value, data);
            }
            if (dataValue.contains("@")) {
                extractListDynamicValue(value, dataValue, data);
            }
        }
    }
    private void extractListDynamicValue(String value, String dataValue, LinkedHashMap<String, String> data){
       try {
           boolean hasIndex = dataValue.contains("[");
           String regex = hasIndex ? "\\@[a-zA-Z0-9\\.\\[\\]\\-\\_\\s]+\\@" : "\\@[a-zA-Z0-9\\.\\-\\s]+\\@";
           Matcher matcher = getMatcherRegex(regex,value,data);
           while (matcher.find()) {
               String dynamicValue = matcher.group();
               String cleaned = dynamicValue.replace("@","").replace("["," ").replace("]","");
               String[] splitDynamicValue = cleaned.split("\\.");
               String sheetName = splitDynamicValue[0].toLowerCase();
               String testcaseId = splitDynamicValue[1];
               String pathAndIndex = splitDynamicValue[2].toLowerCase();

               String pathValue = null;
               int index = 0;
               if(pathAndIndex.contains(" ")){
                   String[] splitPath = pathAndIndex.split(" ");
                   pathValue = splitPath[0];
                   index = Integer.parseInt(splitPath[1]);
               }else {
                   pathValue = pathAndIndex;
               }
               String dataBaseValue = inMemoryDatabaseHelper.getDataFromDataBase(sheetName,testcaseId,pathValue);
               if (!TextUtils.isEmpty(dataBaseValue)){
                   String[] splitDataBaseValue = dataBaseValue.split("\\,");
                   String extractedValue = splitDataBaseValue[index];
                   data.put(value, data.get(value).replace(dynamicValue,extractedValue));
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    private void extractSingleDynamicValue(String value, LinkedHashMap<String, String> data){
        try{
            String regex = "\\#[a-zA-Z0-9\\.\\-\\_\\s]+\\#";
            Matcher matcher = getMatcherRegex(regex,value,data);
            while (matcher.find()){
                String dynamicValue = matcher.group();
                String replaceValueFromDynamicValue = dynamicValue.replace("#", "");
                String[] splitDynamicValue = replaceValueFromDynamicValue.split("\\.");
                String databaseValue = getBaseValue(splitDynamicValue);
                data.put(value, data.get(value).replace(dynamicValue, databaseValue));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    protected Matcher getMatcherRegex(String regex, String value, LinkedHashMap<String, String> data){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(data.get(value));
    }
    private String getBaseValue( String[] splitDynamicValue){
        try {
            String sheetName = splitDynamicValue[0].toLowerCase();
            String testcaseId = splitDynamicValue[1];
            String pathValue = splitDynamicValue[2].toLowerCase();
            return inMemoryDatabaseHelper.getDataFromDataBase(sheetName, testcaseId, pathValue);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
