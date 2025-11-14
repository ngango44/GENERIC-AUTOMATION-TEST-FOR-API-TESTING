package com.framework.utility;

import com.framework.constants.Constants;
import com.framework.inmemorydatabase.InMemoryDatabaseHelper;
import com.google.common.base.Joiner;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GetJsonValuesFromResponse {
    InMemoryDatabaseHelper inMemoryDatabaseHelper = new InMemoryDatabaseHelper();
    private String getJsonPath(String jsonPath){
        String result = "";
        try{
            result = jsonPath.substring(jsonPath.lastIndexOf(".")+1).trim().toLowerCase();
        }catch (Exception e){

        }
        return result;
    }
    public void extractNumber(Response response, String jsonPath, LinkedHashMap<String, String> data, String sheetName){
        try {
            String path = getJsonPath(jsonPath);
            Number numberValue = JsonPath.read(response.getBody().asString(), jsonPath);
            inMemoryDatabaseHelper.createData(data.get(Constants.ExcelColumnNameConstant.TEST_ID.toString()), sheetName, path,
                    numberValue.toString(), Constants.InMemoryDatabaseHelperConstant.TABLENAME.toString());
        }catch (PathNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){

        }
    }
    public void extractString(Response response, String jsonPath, LinkedHashMap<String, String> data, String sheetName){
        try {
            String path = getJsonPath(jsonPath);
            String value = JsonPath.read(response.getBody().asString(), jsonPath);
            inMemoryDatabaseHelper.createData(data.get(Constants.ExcelColumnNameConstant.TEST_ID.toString()), sheetName, path,
                    value, Constants.InMemoryDatabaseHelperConstant.TABLENAME.toString());
        }catch (PathNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){

        }
    }
    public void extractBoolean(Response response, String jsonPath, LinkedHashMap<String, String> data, String sheetName){
        try {
            String path = getJsonPath(jsonPath);
            Boolean booleanValue = JsonPath.read(response.getBody().asString(), jsonPath);
            inMemoryDatabaseHelper.createData(data.get(Constants.ExcelColumnNameConstant.TEST_ID.toString()), sheetName, path,
                    String.valueOf(booleanValue), Constants.InMemoryDatabaseHelperConstant.TABLENAME.toString());
        }catch (PathNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){

        }
    }
    public void extractLong(Response response, String jsonPath, LinkedHashMap<String, String> data, String sheetName){
        try {
            String path = getJsonPath(jsonPath);
            Long longValue = JsonPath.read(response.getBody().asString(), jsonPath);
            inMemoryDatabaseHelper.createData(data.get(Constants.ExcelColumnNameConstant.TEST_ID.toString()), sheetName, path,
                    Long.toString(longValue), Constants.InMemoryDatabaseHelperConstant.TABLENAME.toString());
        }catch (PathNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){

        }
    }
    public void extractStringList(Response response, String jsonPath, LinkedHashMap<String, String> data, String sheetName){
        try {
            String path = getJsonPath(jsonPath);
            List<String> listValues = JsonPath.read(response.getBody().asString(), jsonPath);
            String value = String.join(",",listValues);
            inMemoryDatabaseHelper.createData(data.get(Constants.ExcelColumnNameConstant.TEST_ID.toString()), sheetName, path,
                   value, Constants.InMemoryDatabaseHelperConstant.TABLENAME.toString());
        }catch (PathNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){

        }
    }
    public void extractNumberList(Response response, String jsonPath, LinkedHashMap<String, String> data, String sheetName){
        try {
            String path = getJsonPath(jsonPath);
            List<Number> listValues = JsonPath.read(response.getBody().asString(), jsonPath);
            String value = Joiner.on(',').join(listValues);
            inMemoryDatabaseHelper.createData(data.get(Constants.ExcelColumnNameConstant.TEST_ID.toString()), sheetName, path,
                    value, Constants.InMemoryDatabaseHelperConstant.TABLENAME.toString());
        }catch (PathNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){

        }
    }
    public void extractLongList(Response response, String jsonPath, LinkedHashMap<String, String> data, String sheetName){
        try {
            String path = getJsonPath(jsonPath);
            List<Long> listValues = JsonPath.read(response.getBody().asString(), jsonPath);
            String value = Joiner.on(',').join(listValues);
            inMemoryDatabaseHelper.createData(data.get(Constants.ExcelColumnNameConstant.TEST_ID.toString()), sheetName, path,
                    value, Constants.InMemoryDatabaseHelperConstant.TABLENAME.toString());
        }catch (PathNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){

        }
    }
    public void extractListOfLists(Response response, String jsonPath, LinkedHashMap<String, String> data, String sheetName){
        try {
            String path = getJsonPath(jsonPath);
            List<ArrayList<String>> arrayLists = JsonPath.read(response.getBody().asString(), jsonPath);
            String value = Joiner.on(',').join(arrayLists);
            inMemoryDatabaseHelper.createData(data.get(Constants.ExcelColumnNameConstant.TEST_ID.toString()), sheetName, path,
                    value, Constants.InMemoryDatabaseHelperConstant.TABLENAME.toString());
        }catch (PathNotFoundException e){
            e.printStackTrace();
        }catch (Exception e){

        }
    }
}
