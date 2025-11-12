package com.framework.testcasebase.testsuite;

import com.framework.config.TestConfiguration;
import com.framework.config.TestContext;
import com.framework.testsuite.TestCase;
import com.framework.utility.FactoryHelper;
import org.testng.annotations.Factory;

public class ApiTestFactory {
    /**
     * 1. Scan Excel file
     * 2. Tìm các sheet có thể chạy (Runnable ='Yes')
     * 3. Tạo test instance cho mỗi sheet
     * 4. Trả về array instances cho testNG
     */
    @Factory
    public Object[] runSheet(){
        Object[] responses = null;
        try{
            FactoryHelper factoryHelper = new FactoryHelper();
            TestConfiguration config = factoryHelper.buildConfiguration();

            //Create test instances with injected context
            int totalSheets = config.getTotalSheets();
            responses = new Object[totalSheets];
            for (int i = 0; i<totalSheets;i++){
                TestContext context = new TestContext(i, config);
                responses[i] = new TestCase(context);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return responses;
    }
}
