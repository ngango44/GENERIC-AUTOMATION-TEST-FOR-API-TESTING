package com.framework.restassured;

import com.framework.constants.Constants.RestAssuredHelperConstant;
import com.framework.constants.Constants.ExcelColumnNameConstant;
import com.framework.utility.GetDynamicData;
import io.restassured.response.Response;

import java.util.LinkedHashMap;

public class RestAssuredHelper {
    ApiExecutor apiExecutor = new ApiExecutor();
    GetDynamicData getDynamicData = new GetDynamicData();
    public Response apiExecutorHelper(LinkedHashMap<String, String> data, String sheetName){
        Response res = null;
        String apiType = data.get(ExcelColumnNameConstant.TEST_API_TYPE.toString());
        switch (apiType.toLowerCase()){
            case RestAssuredHelperConstant.GET:
                res = apiExecutor.apiGet(data);
                break;
            case RestAssuredHelperConstant.POST:
                break;
            case RestAssuredHelperConstant.PUT:
                break;
            case RestAssuredHelperConstant.DELETE:
                break;
            case RestAssuredHelperConstant.PATCH:
                break;
            default:
                throw new RuntimeException("No Case Matched For API Type: " + apiType);
        }
        if (res !=null){
            getDynamicData.setCurrentResponse(res);
        }
        return res;
    }
}
