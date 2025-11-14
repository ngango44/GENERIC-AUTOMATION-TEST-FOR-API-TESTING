package com.framework.utility;

import com.framework.constants.Constants.ExcelColumnNameConstant;
import io.restassured.response.Response;
import org.apache.http.util.TextUtils;
import org.testng.Assert;

import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValidationHelper {
    ExtractDynamicData extractDynamicData = new ExtractDynamicData();
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
        String[][] expectedResults = splitMultipleEntriesAndValidate(ExcelColumnNameConstant.TEST_ASSERT_RESPONSE.toString()
                ,";",",",data);
        for(String[] result : expectedResults){
            Assert.assertEquals(result[0],result[1]);
        }
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
}
