package com.APITesting.UserAPIController.Utilities;

import io.restassured.RestAssured;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.baseURI;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import com.APITesting.UserAPIController.Listener.RestAssuredListener;


public class BaseClass {
	
	protected static RequestSpecification requestSpec;
	public static ResourceBundle endpoints = ResourceBundle.getBundle("endpoint");
	public static Response response;
    static String JSONURL = System.getProperty("user.dir")+FileConstants.login_JSON;
	
	
	public static String getBaseURL()
	{
		baseURI = endpoints.getString("baseURL");
		return baseURI;
	}
	public static RequestSpecification getBasicAuthValues() throws IOException
	{
		
		String userData = FileUtils.readFileToString(new File(JSONURL),"UTF-8");
		JSONObject userJsonObj = new JSONObject(userData);
		return RestAssured.given().auth().basic(userJsonObj.getString("username"), userJsonObj.getString("password"));
	}
	public static RequestSpecification restAssuredHeader() throws IOException
	{
		String userData = FileUtils.readFileToString(new File(JSONURL),"UTF-8");
		JSONObject userJsonObj = new JSONObject(userData);
//		PrintStream log = new PrintStream("userApi.txt");
		return RestAssured.given().filter(new RestAssuredListener()).auth().basic(userJsonObj.getString("username"), userJsonObj.getString("password")).baseUri(getBaseURL()).contentType(ContentType.JSON);
	}
}
