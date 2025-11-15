package com.framework.utility;

import com.framework.constants.Constants.RestUtilConstant;
import com.framework.constants.Constants.ExcelColumnNameConstant;
import io.restassured.response.Response;
import org.apache.http.util.TextUtils;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseValueExtractor{
    String restUtilClassName;
    Class<?> restUtilsClass = null;
    Object restUtil = null;
    ValidationHelper validationHelper = new ValidationHelper();
    public ResponseValueExtractor(){
        restUtilClassName = RestUtilConstant.REST_UTIL_CLASSNAME.toString();
        try{
            restUtilsClass = Class.forName(restUtilClassName);
            restUtil = restUtilsClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void extractDynamicResponseValue(Response response, LinkedHashMap<String, String> data){
        String value = ExcelColumnNameConstant.TEST_ASSERT_RESPONSE.toString();
        String dataValue = data.get(value);
        if (dataValue.contains("$.")){
            extractValueFromResponse(response,value,data);
        }
    }
    private void extractValueFromResponse(Response response,String value, LinkedHashMap<String, String> data){
        try {
            String regex = "\\$\\.[a-zA-Z0-9\\[\\]\\.\\-\\_]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(data.get(value));
            while (matcher.find()) {
                String jsonPath = matcher.group();
                String resValue = getValueFromCurrentResponse(response,jsonPath);
                if (resValue != null){
                    data.put(value, data.get(value).replace(jsonPath,resValue));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private String getValueFromCurrentResponse ( Response response,String jsonPath){
        if (response != null){
            return response.jsonPath().getString(jsonPath);
        }
        return null;
    }
    private LinkedHashMap<String, String> separateMethodAndJsonPath(LinkedHashMap<String, String> data){
        LinkedHashMap<String, String> methodAnhJsonPathMap = new LinkedHashMap<>();
        try{
            String columName = ExcelColumnNameConstant.TEST_METHOD_AND_JSON_PATH.toString();
            String methodAndJsonPathString = data.get(columName);
            String[][] entries = validationHelper.splitMultipleEntriesAndValidate(methodAndJsonPathString,";",":",data);
            for (String[] entry: entries){
                // Skip empty entries
                if (entry.length >= 2) {
                    String method = entry[0];
                    String jsonPath = entry[1];
                    methodAnhJsonPathMap.put(method,jsonPath);
                }
            }
        }catch (IllegalArgumentException e){
            System.out.println("Illegal format");
            throw e;
        }catch (Exception e){
            System.err.println("Exception in separateMethodAndJsonPath: " + e);
            System.err.println("Test Case: " + data.get(ExcelColumnNameConstant.TESTCASE_NAME.toString()));
            System.err.println("Test ID: " + data.get(ExcelColumnNameConstant.TEST_ID.toString()));
            System.err.println("Column: " + ExcelColumnNameConstant.TEST_METHOD_AND_JSON_PATH);
            throw e;
        }
        return methodAnhJsonPathMap;
    }
    public void extractAndStoreResponseValues(Response response,LinkedHashMap<String, String> data, String sheetName){
        if (response !=null){
            return;
        }
        if (!TextUtils.isEmpty(data.get(ExcelColumnNameConstant.TEST_METHOD_AND_JSON_PATH.toString()))){
            ResponseValueExtractor extractor = new ResponseValueExtractor();
            LinkedHashMap<String, String> methodAndJsonPathMap = separateMethodAndJsonPath(data);
            extractor.invokeReflection(data,methodAndJsonPathMap,response,sheetName);
        }
    }

    /**
     *
     * @param data -Test data from Excel
     * @param methodAndJsonPathMap - Map of JSONPath to method name
     * @param response
     * @param sheetName
     */
    private void invokeReflection(LinkedHashMap<String, String> data,LinkedHashMap<String, String> methodAndJsonPathMap,Response response, String sheetName) {
        for (Map.Entry<String, String> entry: methodAndJsonPathMap.entrySet()){
            String jsonPath = entry.getKey();
            String method = entry.getValue();
            reflectionHelper(data,method,jsonPath,response,sheetName);
        }
    }
    private void reflectionHelper(LinkedHashMap<String, String> data, String methodName, String jsonPath, Response response, String sheetName) {
        try {
            Method setNameMethod = restUtil.getClass().getMethod(methodName,Response.class, String.class, LinkedHashMap.class,String.class);
            setNameMethod.invoke(restUtil,response,jsonPath,data,sheetName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
