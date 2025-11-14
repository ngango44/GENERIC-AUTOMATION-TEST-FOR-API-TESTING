package com.framework.testsuite;

import com.framework.config.TestContext;
import com.framework.constants.Constants.ExcelColumnNameConstant;
import com.framework.restassured.RestAssuredHelper;
import com.framework.utility.DataUtil;
import com.framework.utility.ExtractDynamicData;
import com.framework.utility.ResponseValueExtractor;
import com.framework.utility.ValidationHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.apache.http.util.TextUtils;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

public class TestCase {
    private final TestContext context;
    private final ExtractDynamicData extractDynamicData = new ExtractDynamicData();
    private final ResponseValueExtractor responseValueExtractor = new ResponseValueExtractor();
    private final RestAssuredHelper restAssuredHelper = new RestAssuredHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    public TestCase(TestContext context){
        this.context = context;
    }
    @Test(priority = 1, dataProvider = "getData", dataProviderClass = DataUtil.class)
    @Description("API Test Execution")
    @Severity(SeverityLevel.CRITICAL)
    public void testMethod(LinkedHashMap<String, String> data){
        String sheetName = context.getSheetName().toLowerCase();
        try {
            String testId = "";
            String testCaseName = "";

            if (!TextUtils.isEmpty(data.get(ExcelColumnNameConstant.TEST_FLOW_NAME.toString()))){
                testId = data.get(ExcelColumnNameConstant.TEST_ID.toString());
                testCaseName = data.get(ExcelColumnNameConstant.TESTCASE_NAME.toString());
                String testName = testId + " - " + testCaseName;

                // Add Allure report info
                Allure.epic(sheetName);
                Allure.feature(data.get(ExcelColumnNameConstant.TEST_FLOW_NAME.toString()));
                Allure.story(testName);
                Allure.parameter("Test ID", testId);
                Allure.parameter("Test Case", testCaseName);
            }

            //Extract dynamic request data
            Allure.step("Extract dynamic request data", () -> {
                extractDynamicData.extractDynamicRequestValue(data);
            });

            //Execute api call
            Response response = Allure.step("Execute API call: " + data.get(ExcelColumnNameConstant.TEST_API_TYPE.toString()), () -> {
                return restAssuredHelper.apiExecutorHelper(data, sheetName);
            });

            //Extract value and store in db
            Allure.step("Extract and store response values", () -> {
                responseValueExtractor.extractAndStoreResponseValues(response, data, sheetName);
            });

            // Extract dynamic response data
            Allure.step("Extract dynamic response data", () -> {
                responseValueExtractor.extractDynamicResponseValue(response, data);
            });

            // verify (status code, schema, assertions)
            Allure.step("Validate results (status code, schema, assertions)", () -> {
                validationHelper.validateResults(response, data);
            });

        }catch (AssertionError e){
            Allure.attachment("Error Details", e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
