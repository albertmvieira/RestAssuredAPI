package tests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import googleAPI.resources;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import utils.reusableMethods;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class basicPostXML {
	
	Properties prop = new Properties();
	
	@BeforeTest
	public void getData() throws IOException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\properties\\env.properties");
		prop.load(fis);
	}

	@Test
	public void postData() throws IOException {

		String postData = GenerateStringFromResource(System.getProperty("user.dir") + "\\src\\main\\java\\googleAPI\\postData.xml");
		
		// BaseURL or Host
		RestAssured.baseURI = prop.getProperty("HOST");

		Response response = given().

				queryParam("key", prop.getProperty("KEY")).
				body(postData).
				when().
				post(resources.placePostDataXML()).
				then().assertThat().statusCode(200).and().contentType(ContentType.XML).
				extract().response();
		

		XmlPath xml = reusableMethods.rawToXML(response);
		String place_id = xml.get("PlaceAddResponse.place_id");
		System.out.println(place_id);
	}
	
	public static String GenerateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}

}
