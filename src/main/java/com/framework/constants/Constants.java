package com.framework.constants;

import java.io.File;

public class Constants {
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String EXCEL_FOLDER_PATH = PROJECT_PATH + File.separatorChar + "src"
            + File.separatorChar + "test" + File.separatorChar + "resources"
            + File.separatorChar + "ExcelData" + File.separatorChar;
    public static class DataUtilConstants {
        public static final StringBuffer RUNMODE = new StringBuffer("Yes");
    }
    public static class ExcelColumnNameConstant {
        public static final StringBuffer TEST_ID = new StringBuffer("test id");
        public static final StringBuffer TEST_MODE = new StringBuffer("test mode");
        public static final StringBuffer TEST_FLOW_NAME = new StringBuffer("test flow name");
        public static final StringBuffer TESTCASE_NAME = new StringBuffer("test case name");
        public static final StringBuffer TEST_API_TYPE = new StringBuffer("test api type");
        public static final StringBuffer TEST_URL = new StringBuffer("test url");
        public static final StringBuffer TEST_INPUT_JSON = new StringBuffer("test input json");
        public static final StringBuffer TEST_HEADERS = new StringBuffer("test headers");
        public static final StringBuffer TEST_SCHEMA_NAME = new StringBuffer("test schema name");
        public static final StringBuffer TEST_EXPECTED_STATUS_CODE = new StringBuffer("test expected status code");
        public static final StringBuffer TEST_PARAMETERS = new StringBuffer("test parameters");
        public static final StringBuffer TEST_METHOD_AND_JSON_PATH = new StringBuffer("test method and json path");
        public static final StringBuffer TEST_ASSERT_RESPONSE = new StringBuffer("test assert response");
    }

    public static class FactoryHelperConstants {
        public static final StringBuffer SHEET_NAME = new StringBuffer("TestSuite");
    }
    public static class InMemoryDatabaseHelperConstant {
        public static final StringBuffer HSQLDBJDBCDRIVER = new StringBuffer("org.hsqldb.jdbc.JDBCDriver");
        public static final StringBuffer CREATINGCONNECTIONFORHSQLDB = new StringBuffer("jdbc:hsqldb:mem:.");
        public static final StringBuffer TABLENAME = new StringBuffer("testcase_response");
        public static final StringBuffer TESTCASEID = new StringBuffer("tcid");
        public static final StringBuffer SHEETNAME = new StringBuffer("sheetname");
        public static final StringBuffer PATHVALUE = new StringBuffer("pathvalue");
        public static final StringBuffer RESPONSEVALUE = new StringBuffer("responsevalue");
    }
    public static class OperatorAssertions{
        public static final StringBuffer CONTAINS = new StringBuffer("verifyContains");
        public static final StringBuffer EQUALS = new StringBuffer("verifyEquals");
        public static final StringBuffer GREATER_THAN = new StringBuffer("verifyGreaterThan");
        public static final StringBuffer LESS_THAN = new StringBuffer("verifyLessThan");
        public static final StringBuffer GREATER_THAN_EQUAL = new StringBuffer("verifyGreaterThanOrEqual");
        public static final StringBuffer LESS_THAN_EQUAL = new StringBuffer("verifyLessThanOrEqual");
    }
    public static class ApiExecutorConstants {
        public static final StringBuffer CONTENT_TYPE = new StringBuffer("application/json");
    }

    public static class RestAssuredHelperConstant {
        public static final String POST = "post";
        public static final String GET = "get";
        public static final String PUT = "put";
        public static final String DELETE = "delete";
        public static final String PATCH = "patch";
    }
    public static class RestUtilConstant {
        public static final StringBuffer REST_UTIL_CLASSNAME = new StringBuffer(
                "com.framework.utility.GetJsonValuesFromResponse");
    }

    public static class ErrorMessageConstant{
        public static final String INVALID_EXPECTED_ERROR = "Invalid Format: [method:]actual,expected ";
    }
}
