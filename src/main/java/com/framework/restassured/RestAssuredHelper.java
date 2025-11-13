package com.framework.restassured;

import com.framework.constants.Constants;
import com.framework.constants.Constants.RestAssuredHelperConstant;
import com.framework.constants.Constants.ExcelColumnNameConstant;
import com.framework.utility.GetDynamicData;
import io.restassured.response.Response;

import java.util.LinkedHashMap;

public class RestAssuredHelper {
    ApiExecutor apiExecutor = new ApiExecutor();
    GetDynamicData getDynamicData = new GetDynamicData();
    public void apiExecutorHelper(LinkedHashMap<String, String> data, String sheetName){
        Response response = null;
        String apiType = data.get(ExcelColumnNameConstant.TEST_API_TYPE.toString());
        switch (apiType.toLowerCase()){
            case RestAssuredHelperConstant.GET:
                response = apiExecutor.apiCallWithType(data, RestAssuredHelperConstant.GET);
                break;
            case RestAssuredHelperConstant.POST:
                response = apiExecutor.apiCallWithType(data, RestAssuredHelperConstant.POST);
                break;
            case RestAssuredHelperConstant.PUT:
                response = apiExecutor.apiCallWithType(data, RestAssuredHelperConstant.PUT);
                break;
            case RestAssuredHelperConstant.DELETE:
                response = apiExecutor.apiCallWithType(data, RestAssuredHelperConstant.DELETE);
                break;
            case RestAssuredHelperConstant.PATCH:
                response = apiExecutor.apiCallWithType(data, RestAssuredHelperConstant.PATCH);
                break;
            default:
                throw new RuntimeException("No Case Matched For API Type: " + apiType);
        }
        if (response !=null){
            getDynamicData.setCurrentResponse(response);
        }
    }
}
