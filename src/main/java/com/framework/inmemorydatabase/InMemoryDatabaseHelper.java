package com.framework.inmemorydatabase;

import java.sql.*;

import com.framework.constants.Constants.InMemoryDatabaseHelperConstant;

public class InMemoryDatabaseHelper {
    private static InMemoryDatabaseHelper instance = null;
    private Statement statement;
    private Connection connectionObj = null;
    int result = 0;

    private InMemoryDatabaseHelper(){
        // Private constructor for Singleton
    }

    public static synchronized InMemoryDatabaseHelper getInstance(){
        if(instance == null){
            instance = new InMemoryDatabaseHelper();
        }
        return instance;
    }

    public void createConnection(){
        try {
            if(connectionObj == null || connectionObj.isClosed()){
                Class.forName(InMemoryDatabaseHelperConstant.HSQLDBJDBCDRIVER.toString());
                connectionObj = DriverManager.getConnection(InMemoryDatabaseHelperConstant.CREATINGCONNECTIONFORHSQLDB.toString());
            }
        }catch (Exception e){
            System.out.println("Exception in Database Connection" + e);
            e.printStackTrace();
        }
    }
    public void createTable(){
        try {
            createConnection();
            statement = connectionObj.createStatement();
            result = statement.executeUpdate("CREATE TABLE "+ InMemoryDatabaseHelperConstant.TABLENAME + "("
                + InMemoryDatabaseHelperConstant.TESTCASEID + " VARCHAR(50),"
                + InMemoryDatabaseHelperConstant.SHEETNAME + " VARCHAR(50),"
                + InMemoryDatabaseHelperConstant.PATHVALUE + " VARCHAR(50),"
                + InMemoryDatabaseHelperConstant.RESPONSEVALUE + " VARCHAR(20000) NOT NULL);");
        }catch (Exception e){
            System.out.println("Exception in create Table for Database: "+ e);
            e.printStackTrace();
        }
    }
    public String getDataFromDataBase(String sheetName, String testcaseId, String pathValue) throws SQLException {
        ResultSet resultFromTable = null;
        try{
            statement = connectionObj.createStatement();
            resultFromTable = statement.executeQuery("select "+ InMemoryDatabaseHelperConstant.TESTCASEID + ","
                + InMemoryDatabaseHelperConstant.PATHVALUE + ","
                + InMemoryDatabaseHelperConstant.RESPONSEVALUE + " from "
                + InMemoryDatabaseHelperConstant.TABLENAME + " where "
                + InMemoryDatabaseHelperConstant.TESTCASEID + "='" + testcaseId + "' AND "
                + InMemoryDatabaseHelperConstant.SHEETNAME + "='" + sheetName + "' AND "
                + InMemoryDatabaseHelperConstant.PATHVALUE + "='" + pathValue + "'");

            while (resultFromTable.next()){
                return resultFromTable.getString(InMemoryDatabaseHelperConstant.RESPONSEVALUE.toString());
            }
        }catch (Exception e){
            System.out.println("Exception in get data from Database: "+ e);
            e.printStackTrace();
        }
        return resultFromTable.getString(InMemoryDatabaseHelperConstant.RESPONSEVALUE.toString());
    }
    public void createData(String testcaseName, String sheetName,String path, String responseValue, String tableName){
        try {
            statement = connectionObj.createStatement();
            result = statement.executeUpdate("INSERT INTO " + tableName + " VALUES ('" + testcaseName + "','" + sheetName + "','" + path + "','" + responseValue + "')");
        }catch (Exception e){
            System.out.println("Exception in createData: " + e);
            e.printStackTrace();
        }
    }
}
