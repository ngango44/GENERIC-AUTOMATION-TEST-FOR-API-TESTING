package com.framework.testsuite;

import com.framework.config.TestContext;
import com.framework.config.TestContextProvider;
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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.LinkedHashMap;

public class TestCase implements TestContextProvider {
    private final TestContext context;
    private final ExtractDynamicData extractDynamicData = new ExtractDynamicData();
    private final ResponseValueExtractor responseValueExtractor = new ResponseValueExtractor();
    private final RestAssuredHelper restAssuredHelper = new RestAssuredHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    public TestCase(TestContext context){
        this.context = context;
    }

    @Override
    public TestContext getContext() {
        return context;
    }

    @Override
    public String toString() {
        String excelFileName = new java.io.File(context.getExcelFilePath()).getName();
        return String.format("TestCase[%s - Sheet: %s - Index: %d]",
            excelFileName, context.getSheetName(), context.getSheetIndex());
    }
    @DataProvider(name = "getDataForInstance")
    public Object[][] getDataForInstance() {
        return new DataUtil().getDataForContext(this.context);
    }

    @Test(priority = 1, dataProvider = "getDataForInstance")
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

                String excelFileName = new File(context.getExcelFilePath()).getName();
                String uniqueTestName = testId + " - " + testCaseName + " [" + excelFileName + "]";

                Allure.getLifecycle().updateTestCase(testResult -> {
                    testResult.setName(uniqueTestName);
                    testResult.setHistoryId(uniqueTestName);
                });

                String fileNameWithoutExtension = excelFileName.replace(".xlsx", "");
                Allure.epic(fileNameWithoutExtension);
                Allure.feature(data.get(ExcelColumnNameConstant.TEST_FLOW_NAME.toString()));
                Allure.story(uniqueTestName);

                Allure.label("tag", fileNameWithoutExtension);

                Allure.parameter("Test ID", testId);
                Allure.parameter("Test Case", testCaseName);
                Allure.parameter("Excel File", excelFileName);
                Allure.parameter("Sheet Name", sheetName);
                Allure.parameter("Sheet Index", context.getSheetIndex());
            }

            Allure.step("Extract dynamic request data", () -> {
                extractDynamicData.extractDynamicRequestValue(data);
            });

            Response response = Allure.step("Execute API call: " + data.get(ExcelColumnNameConstant.TEST_API_TYPE.toString()), () -> {
                return restAssuredHelper.apiExecutorHelper(data, sheetName);
            });

            Allure.step("Extract and store response values", () -> {
                responseValueExtractor.extractAndStoreResponseValues(response, data, sheetName);
            });

            Allure.step("Extract dynamic response data", () -> {
                responseValueExtractor.extractDynamicResponseValue(response, data);
            });

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
