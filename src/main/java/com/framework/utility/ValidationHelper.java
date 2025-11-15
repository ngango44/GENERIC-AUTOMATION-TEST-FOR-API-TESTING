package com.framework.utility;

import com.framework.constants.Constants.OperatorAssertions;
import com.framework.constants.Constants.ExcelColumnNameConstant;
import io.restassured.response.Response;
import org.apache.http.util.TextUtils;
import org.testng.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValidationHelper {
    /**
     *
     * @param rawValue the raw string value from Excel
     * @param entryDelimiter The delimiter between entries (usually ";")
     * @param keyValueDelimiter The delimiter within each entry (usually ":")
     * @param data Test case data for context
     * @return Array of entries
     */
    public String[][] splitMultipleEntriesAndValidate(String rawValue, String entryDelimiter,
                                                      String keyValueDelimiter, LinkedHashMap<String, String> data){
        if (rawValue == null || rawValue.trim().isEmpty()) {
            return new String[0][0];
        }

        String[] entries = data.get(rawValue).split(entryDelimiter);
        String[][] results = new String[entries.length][];
        for (int i = 0; i< entries.length; i++){
            String entry = entries[i].trim();
            if (entry.isEmpty()) {
                results[i] = new String[0];
                continue;
            }
            String[] parts = entry.split(keyValueDelimiter,2);
            if (parts.length >= 2) {
                results[i] = new String[]{parts[0].trim(), parts[1].trim()};
            } else {
                results[i] = new String[0];
            }
        }
        return results;
    }
    private void verifyStatusCode(Response response,LinkedHashMap<String, String> data){
        int actual = response.getStatusCode();
        int expected = Integer.parseInt(data.get(ExcelColumnNameConstant.TEST_EXPECTED_STATUS_CODE.toString()));
        Assert.assertEquals(actual,expected);
    }
    public void validateResults(Response response,LinkedHashMap<String, String> data){
        //status code
        verifyStatusCode(response,data);

        //verify schema
        verifySchemas(response,data);
        //Verify Test expected colum
        verifyResponseExpected(data);
    }
    private void verifySchemas(Response response, LinkedHashMap<String, String> data){
        if (response == null){
            return;
        }
        if (!TextUtils.isEmpty(data.get(ExcelColumnNameConstant.TEST_SCHEMA_NAME.toString()))){
            String schemaName = data.get(ExcelColumnNameConstant.TEST_SCHEMA_NAME.toString());
            try{
                assertThat(response.asString(),matchesJsonSchemaInClasspath("schemas/" + schemaName));
            }catch (AssertionError e){
                e.printStackTrace();
            }
        }
    }
    private void verifyResponseExpected(LinkedHashMap<String, String> data){
        try {
            if (!TextUtils.isEmpty(data.get(ExcelColumnNameConstant.TEST_ASSERT_RESPONSE.toString()))) {
                //doạn này sao ta
                String[][] expectedResults = splitMultipleEntriesAndValidate(ExcelColumnNameConstant.TEST_ASSERT_RESPONSE.toString()
                        , ";", ",", data);
                Field[] fields = OperatorAssertions.class.getDeclaredFields();
                List<String> assertStrings = getAllKeyConstant(fields);
                for (String[] result : expectedResults) {
                    if (result.length >= 2) {
                        if (assertStrings.contains(result[0])) {
                            String[] assertList = result[0].split(":");
                            Method method = this.getClass().getMethod(assertList[0].trim(), String.class, String.class);
                            method.invoke(this,assertList[1].trim(),result[1].trim());
                        }
                        else
                            verifyEquals(result[0].trim(),result[1].trim());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void verifyEquals(String actual, String expected) {
        Assert.assertEquals(actual, expected);
    }
    private void verifyContains(String actual, String expected) {
        Assert.assertTrue(actual.contains(expected));
    }
    private void verifyGreaterThan(String actual, String expected) {
        Number actualNumber = convertStringToNumeric(actual);
        Number expectedNumber = convertStringToNumeric(expected);

        Assert.assertTrue(
                actualNumber.doubleValue() > expectedNumber.doubleValue(),
                "Expected " + actual + " to be greater than " + expected
        );
    }
    private void verifyGreaterThanOrEqual(String actual, String expected) {
        Number actualNumber = convertStringToNumeric(actual);
        Number expectedNumber = convertStringToNumeric(expected);

        Assert.assertTrue(
                actualNumber.doubleValue() > expectedNumber.doubleValue() || actualNumber.doubleValue() == expectedNumber.doubleValue(),
                "Expected " + actual + " to be greater than or equals " + expected
        );
    }
    private void verifyLessThan(String actual, String expected) {
        Number actualNumber = convertStringToNumeric(actual);
        Number expectedNumber = convertStringToNumeric(expected);

        Assert.assertTrue(
                actualNumber.doubleValue() < expectedNumber.doubleValue(),
                "Expected " + actual + " to be less than " + expected
        );
    }
    private void verifyLessThanOrEqual(String actual, String expected) {
        Number actualNumber = convertStringToNumeric(actual);
        Number expectedNumber = convertStringToNumeric(expected);

        Assert.assertTrue(
                actualNumber.doubleValue() < expectedNumber.doubleValue() || actualNumber.doubleValue() == expectedNumber.doubleValue(),
                "Expected " + actual + " to be less than or equals " + expected
        );
    }
    private Number convertStringToNumeric(String value) {
        value = value.trim();

        try {
            // Kiểm tra có phải số thực không
            if (value.contains(".")) {
                return Double.valueOf(value);
            } else {
                // Thử Integer trước
                try {
                    return Integer.valueOf(value);
                } catch (NumberFormatException e) {
                    // Nếu quá lớn thì dùng Long
                    return Long.valueOf(value);
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot convert '" + value + "' to Number");
        }
    }
    private List<String> getAllKeyConstant(Field[] fields){
        List<String> keys = new ArrayList<>();

        for (Field field : fields) {
            if (field.getType() == StringBuffer.class
                    && Modifier.isStatic(field.getModifiers())
                    && Modifier.isFinal(field.getModifiers())) {
                keys.add(field.getName());
            }
        }

        return keys;
    }
}
