package com.framework.restassured;

import com.framework.constants.Constants;
import com.framework.constants.Constants.ExcelColumnNameConstant;
import com.framework.utility.ValidationHelper;
import io.restassured.response.Response;

import java.util.LinkedHashMap;

public class APIExecutorHelper {
    ValidationHelper validationHelper = new ValidationHelper();
    public String getUrl(LinkedHashMap<String, String> data){
        String baseUrl = data.getOrDefault("baseUrl","");
        return baseUrl + data.get(ExcelColumnNameConstant.TEST_URL.toString())
                + data.get(ExcelColumnNameConstant.TEST_PARAMETERS.toString());
    }
    public boolean hasDynamicPlaceholder(String... values){
        for (String value : values) {
            if (value != null && (value.contains("#") || value.contains("@"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Example data: Content-Type:application/json;Authorization:Bearer abc123;X-Custom-Header:test
     */
    public LinkedHashMap<String, String> setHeaders(LinkedHashMap<String, String> data){
        LinkedHashMap<String, String> headerMap = new LinkedHashMap<>();
        String headerString = ExcelColumnNameConstant.TEST_HEADERS.toString();
        if(data.get(headerString).equals(""))
            data.put(headerString,"Content-Type:application/json");

        String[][] headers = validationHelper.splitMultipleEntriesAndValidate(headerString,";",":",data);
        for (String[] header: headers){
            if (header.length >= 2) {
                headerMap.put(header[0].trim(),header[1].trim());
            }
        }
        return headerMap;
    }
}
