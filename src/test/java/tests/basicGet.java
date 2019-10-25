package tests;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.equalTo;

import googleAPI.resources;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class basicGet {
	
	Properties prop = new Properties();
	
	@BeforeTest
	public void getData() throws IOException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\properties\\env.properties");
		prop.load(fis);
	}

	@Test
	public void TestGet() {
		
		
		//BaseURL or Host
		RestAssured.baseURI = prop.getProperty("HOST_Google");
		
		given().
				param("input","Estadio do morumbi").
				param("inputtype","textquery").
				param("key",prop.getProperty("KEY_Google")).
				
				//header, cookie and body should be informed in given() method
				/*
				header("","").
				cookie("","").
				body()
				*/
				
				when().
				get(resources.getGoogleAPI()).
				
				then().
				assertThat().statusCode(200).
				and().contentType(ContentType.JSON).
				and().body("candidates[0].place_id", equalTo("ChIJSeYfG8NWzpQRwhhm_E1PE30"));
	}

}
