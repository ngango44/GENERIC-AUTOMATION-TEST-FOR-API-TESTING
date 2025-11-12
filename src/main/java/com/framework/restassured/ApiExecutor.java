package com.framework.restassured;

import com.framework.constants.Constants;
import com.framework.constants.Constants.ApiExecutorConstants;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.util.TextUtils;
import org.testng.Assert;
import org.testng.SkipException;

import java.util.LinkedHashMap;

import static io.restassured.RestAssured.given;

public class ApiExecutor {
    APIExecutorHelper apiExecutorHelper = new APIExecutorHelper();
    RestAssuredConfig config = RestAssured.config()
            .httpClient(HttpClientConfig.httpClientConfig().setParam("CONNECTION_MANAGER_TIMEOUT",300));

    public Response apiGet(LinkedHashMap<String, String> data){
        String url = apiExecutorHelper.getUrl(data);
        Response res = null;
        try{
            RequestSpecification requestSpecification = given().contentType(ApiExecutorConstants.CONTENT_TYPE.toString())
                    .body(data.get(Constants.ExcelColumnNameConstant.TEST_INPUT_JSON.toString()));
            LinkedHashMap<String, String> headers = new LinkedHashMap<>();
            if (!TextUtils.isEmpty(data.get(Constants.ExcelColumnNameConstant.TEST_HEADERS.toString()))){
                headers = apiExecutorHelper.setHeaders(data);
                if ((apiExecutorHelper.hasDynamicPlaceholder(url,headers.toString(),
                        data.get(Constants.ExcelColumnNameConstant.TEST_INPUT_JSON.toString())))){
                    throw new SkipException("Skipping this exception");
                }
                requestSpecification = requestSpecification.given().headers(headers);
            }
            res = requestSpecification.when().get(url).then().extract().response();
        }catch (Exception e){
            //APIExecutorHelper.logCurlCommand(extentTest);
            Assert.fail("*******************************apiGet: " + e.getMessage());
        }
        return res;
    }
    public Response apiPost(LinkedHashMap<String, String> data){
        String url = apiExecutorHelper.getUrl(data);
        Response res = null;
        try{

        }catch (Exception e){

        }
        return res;
    }
}
