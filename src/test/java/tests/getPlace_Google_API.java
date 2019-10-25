package tests;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utils.reusableMethods;

import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.Test;

import googleAPI.resources;

import static io.restassured.RestAssured.given;

public class getPlace_Google_API {
	
	@Test
	public void TestGet() throws IOException {
		
		Properties prop = reusableMethods.getData();
		
		//BaseURL or Host
		RestAssured.baseURI = prop.getProperty("HOST_Google");
		
		Response response = given().
				param("input","Estadio do morumbi").
				param("inputtype","textquery").
				param("key","AIzaSyDU0zDenVHNvbncvh2z8YMgWRbwdXtTqRM").
				
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
				and().body("candidates[0].place_id", equalTo("ChIJSeYfG8NWzpQRwhhm_E1PE30")).
				extract().response();
		
		JsonPath responseJson = reusableMethods.rawToJson(response);
		String placeId = responseJson.get("candidates[0].place_id");
		System.out.println("placeid: " + placeId);
		
		// Place place_id in the Get request place_Details
		Response resDetails = given().
				param("place_id", placeId).
				param("key","AIzaSyDU0zDenVHNvbncvh2z8YMgWRbwdXtTqRM").
				when().
				get(resources.getPlaceDetail()).
				then().
				assertThat().statusCode(200).
				and().contentType(ContentType.JSON).extract().response();
	
		JsonPath responseJsonDetails = reusableMethods.rawToJson(resDetails);
		
		String name = responseJsonDetails.get("result.name");
		System.out.println(name);
		
		int count = responseJsonDetails.get("result.address_components.size()");
		for (int i = 0; i < count; i++) {
			String long_name = responseJsonDetails.get("result.address_components["+i+"].long_name");
			System.out.println(long_name);
		}
		
	}

}
