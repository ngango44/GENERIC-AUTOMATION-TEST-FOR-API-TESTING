package com.framework.restassured;

import com.framework.constants.Constants;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.util.TextUtils;
import org.testng.Assert;
import org.testng.SkipException;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static io.restassured.RestAssured.given;

public class ApiExecutor {
    APIExecutorHelper apiExecutorHelper = new APIExecutorHelper();
    RestAssuredConfig config = RestAssured.config()
            .httpClient(HttpClientConfig.httpClientConfig().setParam("CONNECTION_MANAGER_TIMEOUT",300));

    public Response apiCallWithType(LinkedHashMap<String, String> data, String method){
        String url = apiExecutorHelper.getUrl(data);
        Response response = null;
        try{
            RequestSpecification request = given()
                    .filter(new AllureRestAssured()); // Add Allure filter to log request/response
            LinkedHashMap<String, String> headers = apiExecutorHelper.setHeaders(data);
            String body = data.get(Constants.ExcelColumnNameConstant.TEST_INPUT_JSON.toString());
            request.headers(headers !=null? headers: new HashMap<>());
            if (apiExecutorHelper.hasDynamicPlaceholder(url,headers.toString(), body)){
                throw new SkipException("Skipping this exception");
            }
            if(body !=null){
                request.body(body);
            }
            response = request.request(method.toUpperCase(), url);
        }catch (Exception e){
            //APIExecutorHelper.logCurlCommand(extentTest);
            Assert.fail("*******************************apiGet: " + e.getMessage());
        }
        return response;
    }
}
