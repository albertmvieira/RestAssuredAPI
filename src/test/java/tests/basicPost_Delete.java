package tests;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import googleAPI.payLoad;
import googleAPI.resources;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utils.reusableMethods;


public class basicPost_Delete {

	final static Logger log = Logger.getLogger(basicPost_Delete.class);
	
	Properties prop = new Properties();
	
	@BeforeTest
	public void getData() throws IOException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\properties\\env.properties");
		prop.load(fis);
		PropertyConfigurator.configure("log4j.properties");
	}
	
	@Test
	public void AddandDeletePlace() {

		// Task 1 - Grab the response
		
		log.info("Host information: " + prop.getProperty("HOST"));
		RestAssured.baseURI = prop.getProperty("HOST");

		Response res = given().

		queryParam("key", prop.getProperty("KEY")).
		body(payLoad.getPostData()).
		when().
		post(resources.placePostDataJson()).
		then().
		assertThat().statusCode(200).and().
		contentType(ContentType.JSON).and().
		body("status", equalTo("OK")).
		extract().response();

		// Task 2 - Garb the placeId from response
		JsonPath responseJson = reusableMethods.rawToJson(res);
		String placeId = responseJson.get("place_id");
		log.info("placeid: " + placeId);
		
		// Task 3 - place this place_id in the Delete request
		given().
		queryParam("key", prop.getProperty("KEY")).
		body("{" + "\"place_id\": \"" + placeId + "\"" + "}").
		when().
		post(resources.placeDeleteData()).
		then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
		body("status", equalTo("OK"));
	}

}
