package tests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import googleAPI.payLoad;
import googleAPI.resources;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.equalTo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class basicPost {
	
	Properties prop = new Properties();
	
	@BeforeTest
	public void getData() throws IOException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\properties\\env.properties");
		prop.load(fis);
	}

	@Test
	public void postData() {

		// BaseURL or Host
		RestAssured.baseURI = prop.getProperty("HOST");

		given().

				queryParam("key", prop.getProperty("KEY")).

				body(payLoad.getPostData())
				.when()
				.post(resources.placePostDataJson())
				.then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and()
				.body("status", equalTo("OK"));
	}

}
