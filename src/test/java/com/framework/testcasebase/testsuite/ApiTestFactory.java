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
            System.out.println("=== DEBUG: ApiTestFactory starting...");
            FactoryHelper factoryHelper = new FactoryHelper();
            TestConfiguration config = factoryHelper.buildConfiguration();

            //Create test instances with injected context
            int totalSheets = config.getTotalSheets();
            System.out.println("=== DEBUG: Creating " + totalSheets + " test instances");
            responses = new Object[totalSheets];
            for (int i = 0; i<totalSheets;i++){
                TestContext context = new TestContext(i, config);
                responses[i] = new TestCase(context);
                System.out.println("=== DEBUG: Created test instance " + (i+1) + " for sheet: " + context.getSheetName());
            }
            System.out.println("=== DEBUG: Factory returning " + responses.length + " test instances");
        }catch (Exception e){
            System.err.println("=== ERROR in Factory:");
            e.printStackTrace();
        }
        return responses;
    }
}
