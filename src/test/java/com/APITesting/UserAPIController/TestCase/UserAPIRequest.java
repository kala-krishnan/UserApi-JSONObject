package com.APITesting.UserAPIController.TestCase;

import java.io.File;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.APITesting.UserAPIController.Utilities.BaseClass;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class UserAPIRequest extends BaseClass{
	@Test(dataProvider = "getUserDetails")
	
	public void postUsers(String name,String job) throws IOException
	{
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", "User");
		jsonObj.put("job", "Tester");
		
		System.out.println("JSON String is" +jsonObj.toString());
		 response = RestAssured.given().baseUri("https://reqres.in/").contentType(ContentType.JSON)
				    .body(jsonObj.toString())
				    .when()
				    .post("/api/users")
				    .then()
				    .assertThat().statusCode(201)
				    .extract()
				    .response();
		 
		 System.out.println("\n Response.status code " +response.getStatusCode());
	}
	@DataProvider(name="getUserDetails")
	public Object[] getValuesFromDataProvider() throws IOException
	{
		String getValuesFromJSONFile = System.getProperty("user.dir")+"//src//test//resources//JsonFiles//PostUser.json";;
		String jsonContent = new String(Files.readAllBytes(Paths.get(getValuesFromJSONFile)));
		JSONArray jsonArray = new JSONArray(jsonContent);
		Object[][] testData = new Object[jsonArray.length()][];
		for (int i=0;i<jsonArray.length();i++)
		{
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			Object[] id=
					{
							jsonObj.getString("name"),
							jsonObj.getString("job"),
							};
			testData[i]=id;
			}
		return testData;
	}
	
	

}
