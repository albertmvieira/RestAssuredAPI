package tests;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.testng.annotations.Test;

import libraryAPI.resources;
import utils.reusableMethods;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class StaticJson {
	
	@Test
	public void addBook() throws IOException {

		String postData = GenerateStringFromResource(System.getProperty("user.dir") + "\\src\\main\\java\\libraryAPI\\addBook.json");
		Properties prop = reusableMethods.getData();
		
		// Task 1 - Grab the response
		RestAssured.baseURI = prop.getProperty("HOST");

		Response res = given().
				header("Content-Type", "application/json").

		body(postData).
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
	
	public static String GenerateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}
}
