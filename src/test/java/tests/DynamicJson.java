package tests;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import libraryAPI.payLoad;
import libraryAPI.resources;
import utils.reusableMethods;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class DynamicJson {
	
	@Test(dataProvider="BooksData")
	public void addBook(String isbn, String aisle) throws IOException {
		
		Properties prop = reusableMethods.getData();

		// Task 1 - Grab the response
		RestAssured.baseURI = prop.getProperty("HOST");

		Response res = given().
				header("Content-Type", "application/json").

		body(payLoad.addBook(isbn, aisle)).
		when().
		post(resources.addBook()).
		then().
		assertThat().statusCode(200).and().
		contentType(ContentType.JSON).and().
		extract().response();

		// Task 2 - Garb the ID from response
		JsonPath responseJson = reusableMethods.rawToJson(res);
		String id = responseJson.get("ID");
		System.out.println("ID: " + id);
		
		
		// Task 3 - place this id in the Delete request
		Response delete = given().
		header("Content-Type", "application/json").
		body("{" + "\"ID\": \"" + id + "\"" + "}").
		when().
		post(resources.deleteBook()).
		then().assertThat().statusCode(200).
			and().contentType(ContentType.JSON).
			and().body("msg", equalTo("book is successfully deleted")).extract().response();
		
		reusableMethods.rawToJson(delete);
		
	}
	
	@DataProvider(name="BooksData")
	public Object [][] getData(){
		//array = collection of elements
		//multidimensional array = collection of arrays
		return new Object [][] {{"albert","007"},{"albert","008"},{"albert","009"}};
	}

}
