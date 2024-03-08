package com.APITesting.UserAPIController.TestCase;

import java.io.File;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.APITesting.UserAPIController.Utilities.BaseClass;
import com.APITesting.UserAPIController.Utilities.FileConstants;

import io.restassured.module.jsv.JsonSchemaValidator;
import static org.hamcrest.Matchers.equalTo;





public class ApiRequest extends BaseClass{
	
	int responseCode =0;
	private int userID=0;
	private String userName="";
	private int addressID=0;
	static String postJSONFile = System.getProperty("user.dir")+FileConstants.Post_JSON;
	static String postNegJSONFile = System.getProperty("user.dir")+FileConstants.POST_Neg_JSON;
	static String putJSONFile = System.getProperty("user.dir")+FileConstants.PUT_JSON;
	String userSchemaFile = System.getProperty("user.dir")+FileConstants.user_Schema;
	static String getUserJSONFile = System.getProperty("user.dir")+FileConstants.UserId_Neg_JSON;
	static String getUserNameJSONFile = System.getProperty("user.dir")+FileConstants.UserName_Neg_JSON;
	static String putNegJSONFile = System.getProperty("user.dir")+FileConstants.PUT_Neg_JSON;
			
	
	private static final Logger logger = LogManager.getLogger(ApiRequest.class);
	
	@Test(dataProvider = "PostRequestPositive",priority =1,groups = {"E2E"})
	public void postUsersPositive(String FirstName,String LastName,String ContactNumber,String emailId,String plotNumber,String street,String state,
			String country,String zipcode) throws IOException
	{
		logger.info("***********************User API E2E Testing *****************");
		String jsonSchema = FileUtils.readFileToString(new File(userSchemaFile),StandardCharsets.UTF_8);
		JSONObject jsonObj = new JSONObject();
		JSONObject nestedJsonObj = new JSONObject();
		jsonObj.put("user_first_name", FirstName);
		jsonObj.put("user_last_name", LastName);
		jsonObj.put("user_contact_number", ContactNumber);
		jsonObj.put("user_email_id", emailId);
		
		nestedJsonObj.put("plotNumber", plotNumber);
		nestedJsonObj.put("street",street);
		nestedJsonObj.put("state", state);
		nestedJsonObj.put("country",country);
		nestedJsonObj.put("zipCode", zipcode);
		jsonObj.put("userAddress", nestedJsonObj);
		
		 response = restAssuredHeader()
				    .body(jsonObj.toString())
				    .when()
				    .post(endpoints.getString("PostURl"))
				    .then()
				    .assertThat().statusCode(201)
				    .extract()
				    .response();
		 responseCode = response.statusCode();	
		logger.info("Status Code in post request " +response.statusCode());
		logger.info("Status Code in post request " +response.jsonPath().getInt("user_id"));
		 if(response.getStatusCode() == Integer.parseInt(endpoints.getString("createCode")))
		 {
		 userID=response.jsonPath().getInt("user_id");
		 userName=response.jsonPath().getString("user_first_name");
		 addressID=response.jsonPath().getInt("userAddress.addressId");
		 }
		 response.then().body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
		
	}
	
	@Test(priority =2,groups = {"E2E"})
	public void getAllUsers() throws IOException
	{
		String jsonSchema = FileUtils.readFileToString(new File(userSchemaFile),StandardCharsets.UTF_8);
		 response = restAssuredHeader().when()
				    .get(endpoints.getString("getURL"))
				    .then().assertThat().statusCode(200).extract().response();
			responseCode = response.statusCode();
			logger.info("Status Code in Get all request " +response.statusCode());
			
//			int resposeLength = response.getBody().toString().split("\"user_id\"").length - 1;
//			System.out.println("Responsse lenght" +resposeLength);
	}
	
	@Test(priority =3,groups = {"E2E"})
	public void getUserById() throws IOException
	{
		String jsonSchema = FileUtils.readFileToString(new File(userSchemaFile),StandardCharsets.UTF_8);
		 response = restAssuredHeader()
				    .pathParam("userId", userID).when()
				    .get(endpoints.getString("getbyIdURL"))
				    .then().assertThat().statusCode(200).extract().response();
			responseCode = response.statusCode();
			 logger.info("Status Code in Get by id request " +response.statusCode());
			 logger.info("Validating Get User Id Respone ==>"+response.then().body("user_id", equalTo(userID)));
			 
	}
	
	@Test(dataProvider = "PutRequest",priority =4,groups = {"E2E"})
	public void putUser(String FirstName,String LastName,String ContactNumber,String emailId,String plotNumber,String street,String state,
			String country,String zipcode) throws IOException
	{
		String jsonSchema = FileUtils.readFileToString(new File(userSchemaFile),StandardCharsets.UTF_8);
		JSONObject jsonObj1 = new JSONObject();
		JSONObject nestedJsonObj1 = new JSONObject();
		jsonObj1.put("user_id", userID);
		jsonObj1.put("user_first_name", FirstName);
		jsonObj1.put("user_last_name", LastName);
		jsonObj1.put("user_contact_number", ContactNumber);
		jsonObj1.put("user_email_id", emailId);
		nestedJsonObj1.put("addressId", addressID);
		nestedJsonObj1.put("plotNumber", plotNumber);
		nestedJsonObj1.put("street", street);
		nestedJsonObj1.put("state", state);
		nestedJsonObj1.put("country", country);
		nestedJsonObj1.put("zipCode", zipcode);
		jsonObj1.put("userAddress", nestedJsonObj1);
		 response = restAssuredHeader()
				 	.pathParam("userId", userID).when()
				    .body(jsonObj1.toString())    
				    .put(endpoints.getString("PUTURL"))
				    .then().assertThat().statusCode(200).extract().response();
				    
			responseCode = response.statusCode();
			 logger.info("Status Code in put user request " +response.statusCode());
			
			
	}
	
	@Test(priority =5,groups = {"E2E"})
	public void getUserByName() throws IOException
	{
		String jsonSchema = FileUtils.readFileToString(new File(userSchemaFile),StandardCharsets.UTF_8);
		 response =  restAssuredHeader()
				    .pathParam("userFirstName", userName).when()
				    .get(endpoints.getString("getByNameURL"))
				    .then().assertThat().statusCode(200).extract().response();
			responseCode = response.statusCode();
			 logger.info("Status Code in user name request " +response.statusCode());
		//	 logger.info("Validating Get User Id Respone ==>"+response.then().body("user_first_name", equalTo(userName)));
			 
				
	}
	
	@Test(priority =6,groups = {"E2E"})
	public void deleteUserById() throws IOException
	{
		 response = restAssuredHeader()
				    .pathParam("userId",userID).when()
				    .delete("/deleteuser/{userId}")
				    .then().assertThat().statusCode(200).extract().response();
			responseCode = response.statusCode();
			 logger.info("Status Code in delete id request " +response.statusCode());
			 logger.info("Validating Get User Id Respone ==>"+response.then().body("status", equalTo("Success")));
				
	}
	
	@Test(priority =7)
	public void deleteUserByName() throws IOException
	{
		 response = restAssuredHeader()
				    .pathParam("userFirstName", userName).when()
				    .delete(endpoints.getString("deleteByNameURL"))
				    .then().assertThat().statusCode(400).extract().response();
		 System.out.println("What is the status code outside" +response.statusCode());
		 if(response.statusCode() == 400)
		 {
			 System.out.println("What is the status code" +response.statusCode());
			 JSONObject jsonObj = new JSONObject();
				JSONObject nestedJsonObj = new JSONObject();
				jsonObj.put("user_first_name", "honda");
				jsonObj.put("user_last_name", "Activa");
				jsonObj.put("user_contact_number", 4352435243L);
				jsonObj.put("user_email_id", "hondaActiva@gmail.com");
				
				nestedJsonObj.put("plotNumber", "D-12");
				nestedJsonObj.put("Street","SundarNagar");
				nestedJsonObj.put("state", "Chennai");
				nestedJsonObj.put("Country","India");
				nestedJsonObj.put("zipCode", 600023);
				jsonObj.put("userAddress", nestedJsonObj);
				
				 response = restAssuredHeader()
						    .body(jsonObj.toString())
						    .when()
						    .post(endpoints.getString("PostURl"))
						    .then()
						    .assertThat().statusCode(201)
						    .extract()
						    .response();
				 if(response.getStatusCode()==201)
				 {
					 String name = response.jsonPath().getString("user_first_name");
					 response = restAssuredHeader()
							    .pathParam("userFirstName", name).when()
							    .delete(endpoints.getString("deleteByNameURL"))
							    .then().assertThat().statusCode(200).extract().response();
				 }
		 }
			responseCode = response.statusCode();
			 logger.info("Status Code in delete name request " +response.statusCode());
			 logger.info("Validating Get User Id Respone ==>"+response.then().body("status", equalTo("Success")));
			logger.info("***********************User API E2E Testing *****************");	
	}
	@Test(dataProvider = "PostRequestNegative",priority =8,groups= {"NegativeTest"})
	public void postUsersNegative(String FirstName,String LastName,String ContactNumber,String emailId,String plotNumber,String street,String state,
			String country,String zipcode) throws IOException
	{
		logger.info("***********************User Negative Testing *****************");
		String jsonSchema = FileUtils.readFileToString(new File(userSchemaFile),
				StandardCharsets.UTF_8);
		JSONObject jsonObj = new JSONObject();
		JSONObject nestedJsonObj = new JSONObject();
		jsonObj.put("user_first_name", FirstName);
		jsonObj.put("user_last_name", LastName);
		jsonObj.put("user_contact_number", ContactNumber);
		jsonObj.put("user_email_id", emailId);
		
		nestedJsonObj.put("plotNumber", plotNumber);
		nestedJsonObj.put("street",street);
		nestedJsonObj.put("state", state);
		nestedJsonObj.put("country",country);
		nestedJsonObj.put("zipCode", zipcode);
		jsonObj.put("userAddress", nestedJsonObj);
		
		 response = restAssuredHeader()
				    .body(jsonObj.toString())
				    .when()
				    .post(endpoints.getString("PostURl"))
				    .then()
				    .assertThat().statusCode(Matchers.either(Matchers.is(400)).or(Matchers.is(409)).or(Matchers.is(404)))
				    .extract()
				    .response();
		 responseCode = response.statusCode();	
		logger.info("Status Code in post request " +response.statusCode());
	}
	
	@Test(dataProvider = "GetRequestNegative",priority =9,groups = {"NegativeTest"})
	public void getUserByIdNegative(String user_id) throws IOException
	{
		
		 response = restAssuredHeader()
				    .pathParam("userId", user_id).when()
				    .get(endpoints.getString("getbyIdURL"))
				    .then().assertThat().statusCode(Matchers.either(Matchers.is(400)).or(Matchers.is(404)).or(Matchers.is(404))).extract().response();
			responseCode = response.statusCode();
			 logger.info("Status Code in Get by id request " +response.statusCode());
	}
	@Test(dataProvider = "GetNameRequestNegative",priority =10,groups = {"NegativeTest"})
	public void getUserByNameNegative(String userName) throws IOException
	{
		 response =  restAssuredHeader()
				    .pathParam("userFirstName", userName).when()
				    .get(endpoints.getString("getByNameURL"))
				    .then().assertThat().statusCode(Matchers.either(Matchers.is(400)).or(Matchers.is(404)).or(Matchers.is(404))).extract().response();
			responseCode = response.statusCode();
			 logger.info("Status Code in user name request " +response.statusCode());
				
	}
	@Test(dataProvider = "GetRequestNegative",priority =11,groups = {"NegativeTest"})
	public void deleteUserByIdNegative(String user_id) throws IOException
	{
		 response = restAssuredHeader()
				    .pathParam("userId",userID).when()
				    .delete("/deleteuser/{userId}")
				    .then().assertThat().statusCode(404).extract().response();
			responseCode = response.statusCode();
			 logger.info("Status Code in delete id request " +response.statusCode());
				
	}
	
	@Test(dataProvider = "GetNameRequestNegative",priority =12,groups = {"NegativeTest"})
	public void deleteUserByNameNegative(String userName) throws IOException
	{
		 response = restAssuredHeader()
				    .pathParam("userFirstName", userName).when()
				    .delete(endpoints.getString("deleteByNameURL"))
				    .then().assertThat().statusCode(404).extract().response();
			responseCode = response.statusCode();
			 logger.info("Status Code in delete name request " +response.statusCode());
				
			logger.info("***********************Negative Testing *****************");	
	}
	
	@Test(dataProvider = "PutRequestNegative",priority =13,groups = {"NegativeTest"})
	public void putUserNegative(String FirstName,String LastName,String ContactNumber,String emailId,String plotNumber,String street,String state,
			String country,String zipcode) throws IOException
	{
		String jsonSchema = FileUtils.readFileToString(new File(userSchemaFile),StandardCharsets.UTF_8);
		JSONObject jsonObj1 = new JSONObject();
		JSONObject nestedJsonObj1 = new JSONObject();
		jsonObj1.put("user_id", 8720);
		jsonObj1.put("user_first_name", FirstName);
		jsonObj1.put("user_last_name", LastName);
		jsonObj1.put("user_contact_number", ContactNumber);
		jsonObj1.put("user_email_id", emailId);
		nestedJsonObj1.put("addressId", 8703);
		nestedJsonObj1.put("plotNumber", plotNumber);
		nestedJsonObj1.put("street", street);
		nestedJsonObj1.put("state", state);
		nestedJsonObj1.put("country", country);
		nestedJsonObj1.put("zipCode", zipcode);
		jsonObj1.put("userAddress", nestedJsonObj1);
		 response = restAssuredHeader()
				 	.pathParam("userId", userID).when()
				    .body(jsonObj1.toString())    
				    .put(endpoints.getString("PUTURL"))
				    .then().assertThat().statusCode(Matchers.either(Matchers.is(400)).or(Matchers.is(409)).or(Matchers.is(404))).extract().response();
				    
			responseCode = response.statusCode();
			 logger.info("Status Code in put user request " +response.statusCode());
			
			
	}
	
	
	
	
	
	//********************************Data Providers*********************************************//
	
	
	
	@DataProvider(name="PostRequestPositive")
	public Object[][] getRecordsFromJSONForPostPositive() throws IOException
	{
		String jsonContent = new String(Files.readAllBytes(Paths.get(postJSONFile)));
		JSONArray jsonArray = new JSONArray(jsonContent);
		
		Object[][] testData = new Object[jsonArray.length()][];
		for (int i=0;i<jsonArray.length();i++)
		{
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			Object[] userData = {
				jsonObj.getString("user_first_name"),	
				jsonObj.getString("user_last_name"),
				jsonObj.getString("user_contact_number"),
				jsonObj.getString("user_email_id"),
				jsonObj.getJSONObject("userAddress").getString("plotNumber"),
				jsonObj.getJSONObject("userAddress").getString("street"),
				jsonObj.getJSONObject("userAddress").getString("state"),
				jsonObj.getJSONObject("userAddress").getString("country"),
				jsonObj.getJSONObject("userAddress").getString("zipCode")
			};
			testData[i]=userData;	
			}
		return testData;
	}
	@DataProvider(name="PutRequest")
	public Object[][] getRecordsFromJSONForPut() throws IOException
	{
		String jsonContent = new String(Files.readAllBytes(Paths.get(putJSONFile)));
		JSONArray jsonArray = new JSONArray(jsonContent);
		
		Object[][] testData = new Object[jsonArray.length()][];
		for (int i=0;i<jsonArray.length();i++)
		{
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			Object[] userData = {
				jsonObj.getString("user_first_name"),	
				jsonObj.getString("user_last_name"),
				jsonObj.getString("user_contact_number"),
				jsonObj.getString("user_email_id"),
				jsonObj.getJSONObject("userAddress").getString("plotNumber"),
				jsonObj.getJSONObject("userAddress").getString("street"),
				jsonObj.getJSONObject("userAddress").getString("state"),
				jsonObj.getJSONObject("userAddress").getString("country"),
				jsonObj.getJSONObject("userAddress").getString("zipCode")
			};
			testData[i]=userData;	
			}
		return testData;
		
	}
	@DataProvider(name="PostRequestNegative")
	public Object[][] getRecordsFromJSONForPost() throws IOException
	{
		String jsonContent = new String(Files.readAllBytes(Paths.get(postNegJSONFile)));
		JSONArray jsonArray = new JSONArray(jsonContent);
		
		Object[][] testData = new Object[jsonArray.length()][];
		for (int i=0;i<jsonArray.length();i++)
		{
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			Object[] userData = {
				jsonObj.getString("user_first_name"),	
				jsonObj.getString("user_last_name"),
				jsonObj.getString("user_contact_number"),
				jsonObj.getString("user_email_id"),
				jsonObj.getJSONObject("userAddress").getString("plotNumber"),
				jsonObj.getJSONObject("userAddress").getString("street"),
				jsonObj.getJSONObject("userAddress").getString("state"),
				jsonObj.getJSONObject("userAddress").getString("country"),
				jsonObj.getJSONObject("userAddress").getString("zipCode")
			};
			testData[i]=userData;	
			}
		return testData;
	}
	@DataProvider(name="GetRequestNegative")
	public Object[] getUserIdforNegative() throws IOException
	{
		String jsonContent = new String(Files.readAllBytes(Paths.get(getUserJSONFile)));
		JSONArray jsonArray = new JSONArray(jsonContent);
		
		Object[][] testData = new Object[jsonArray.length()][];
		for (int i=0;i<jsonArray.length();i++)
		{
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			Object[] id=
					{
							jsonObj.getString("user_id")
							};
			testData[i]=id;
			}
		return testData;
	}
	
	@DataProvider(name="GetNameRequestNegative")
	public Object[] getUserNameforNegative() throws IOException
	{
		String jsonContent = new String(Files.readAllBytes(Paths.get(getUserNameJSONFile)));
		JSONArray jsonArray = new JSONArray(jsonContent);
		
		Object[][] testData = new Object[jsonArray.length()][];
		for (int i=0;i<jsonArray.length();i++)
		{
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			Object[] id=
					{
							jsonObj.getString("user_first_name")
							};
			testData[i]=id;
			}
		return testData;
	}
	@DataProvider(name="PutRequestNegative")
	public Object[][] getRecordsFromJSONForPutNegative() throws IOException
	{
		String jsonContent = new String(Files.readAllBytes(Paths.get(putNegJSONFile)));
		JSONArray jsonArray = new JSONArray(jsonContent);
		
		Object[][] testData = new Object[jsonArray.length()][];
		for (int i=0;i<jsonArray.length();i++)
		{
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			Object[] userData = {
				jsonObj.getString("user_first_name"),	
				jsonObj.getString("user_last_name"),
				jsonObj.getString("user_contact_number"),
				jsonObj.getString("user_email_id"),
				jsonObj.getJSONObject("userAddress").getString("plotNumber"),
				jsonObj.getJSONObject("userAddress").getString("street"),
				jsonObj.getJSONObject("userAddress").getString("state"),
				jsonObj.getJSONObject("userAddress").getString("country"),
				jsonObj.getJSONObject("userAddress").getString("zipCode")
			};
			testData[i]=userData;	
			}
		return testData;
		
	}
	
}


