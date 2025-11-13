package com.framework.utility;

import com.framework.constants.Constants.ExcelColumnNameConstant;
import com.framework.restassured.RestAssuredHelper;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.Assertion;

import java.util.LinkedHashMap;

public class ValidationHelper {
    GetDynamicData getDynamicData = new GetDynamicData();
    Response response = getDynamicData.getCurrentResponse();
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
        String[] entries = rawValue.split(entryDelimiter);
        String[][] results = new String[entries.length][];
        for (int i = 0; i< entries.length; i++){
            String entry = entries[i].trim();
            String[] parts = entry.split(keyValueDelimiter,2);
            results[i] = new String[]{ parts[0].trim(),parts[1].trim()};
        }
        return results;
    }

    private void verifyStatusCode(LinkedHashMap<String, String> data){
        int actual = response.getStatusCode();
        int expected = Integer.parseInt(data.get(ExcelColumnNameConstant.TEST_EXPECTED_STATUS_CODE.toString()));
        Assert.assertEquals(actual,expected);
    }
    public void validateResults(LinkedHashMap<String, String> data){
        //status code
        verifyStatusCode(data);

        //Verify Test expected colum
        String[][] expectedResults = splitMultipleEntriesAndValidate(ExcelColumnNameConstant.TEST_ASSERT_RESPONSE.toString()
                ,";",",",data);
        for(String[] result : expectedResults){
            Assert.assertEquals(result[0],result[1]);
        }
        //remove current response
        getDynamicData.clearCurrentResponse();
    }
}
