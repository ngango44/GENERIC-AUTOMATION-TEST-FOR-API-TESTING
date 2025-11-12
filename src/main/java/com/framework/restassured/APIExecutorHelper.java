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
        String headerString = data.get(ExcelColumnNameConstant.TEST_HEADERS.toString());

        String[][] headers = validationHelper.splitMultipleEntriesAndValidate(headerString,";",":",data);
        for (String[] header: headers){
            headerMap.put(header[0],header[1]);
        }
        return headerMap;
    }
}
