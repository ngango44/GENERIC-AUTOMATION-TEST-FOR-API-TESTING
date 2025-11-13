package com.framework.testsuite;

import com.framework.config.TestContext;
import com.framework.constants.Constants.ExcelColumnNameConstant;
import com.framework.restassured.RestAssuredHelper;
import com.framework.utility.DataUtil;
import com.framework.utility.GetDynamicData;
import com.framework.utility.ValidationHelper;
import io.restassured.response.Response;
import org.apache.http.util.TextUtils;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

public class TestCase {
    private final TestContext context;
    private final GetDynamicData getDynamicData = new GetDynamicData();
    private final RestAssuredHelper restAssuredHelper = new RestAssuredHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    public TestCase(TestContext context){
        this.context = context;
    }
    @Test(priority = 1, dataProvider = "getData", dataProviderClass = DataUtil.class)
    public void testMethod(LinkedHashMap<String, String> data){
        String sheetName = context.getSheetName().toLowerCase();
        try {
            if (!TextUtils.isEmpty(data.get(ExcelColumnNameConstant.TEST_FLOW_NAME.toString()))){
                String testId = data.get(ExcelColumnNameConstant.TEST_ID.toString());
                String testCaseName = data.get(ExcelColumnNameConstant.TESTCASE_NAME.toString());
                String testName = testId + " - " +testCaseName;
            }
            getDynamicData.extractDynamicRequestValue(data);
            restAssuredHelper.apiExecutorHelper(data,sheetName);
            getDynamicData.extractDynamicResponseValue(data);
            validationHelper.validateResults(data);
        }catch (AssertionError e){
            e.printStackTrace();
        }
    }
}
