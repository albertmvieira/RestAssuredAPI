package tests;

import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import CourseOAuth2.resourcesCourseOAuth2;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utils.reusableMethods;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Properties;

public class Oauth2GetCourse {
	
	Properties prop;
	
	public String getAccessCode() throws InterruptedException, IOException {
		
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\java\\drivers\\chromedriver.exe");

		// CHROME OPTIONS
		WebDriver driver= new ChromeDriver();
		driver.manage().window().maximize();

		driver.get(prop.getProperty("Host_AuthServer"));

		driver.findElement(By.cssSelector("[type='email']")).sendKeys("restassuredtestapi@gmail.com");

		driver.findElement(By.cssSelector("[type='email']")).sendKeys(Keys.ENTER);

		Thread.sleep(3000);

		driver.findElement(By.cssSelector("[type='password']")).sendKeys("Teste123");

		driver.findElement(By.cssSelector("[type='password']")).sendKeys(Keys.ENTER);

		Thread.sleep(5000);
		
		
		//Pegando URL e extraindo o accessCode

		String url = driver.getCurrentUrl();

		System.out.println(url);
		
		String code = url.split("code=")[1];
		
		code = code.split("&scope")[0];
		
		System.out.println(code);
		
		driver.quit();
		
		return code;
			
	}
	
	public String getAccessToken() throws InterruptedException, IOException {
		
		RestAssured.baseURI = prop.getProperty("Host_AccessToken");
		
		Response res = given().
		urlEncodingEnabled(false). //tratando caracter especial % para não ser convertido com encode
		queryParams("code", getAccessCode()).
		queryParams("client_id", prop.getProperty("Course_clientId")).
		queryParams("client_secret", prop.getProperty("Course_clientSecret")).
		queryParams("redirect_uri", prop.getProperty("Course_redirect_uri")).
		queryParams("grant_type", prop.getProperty("Course_grant_type")).
		when().log().all().
		post().
		then().
		extract().response();
		//assertThat().statusCode(200).extract().response();
		
	
		//convert to json
		JsonPath responseJson = reusableMethods.rawToJson(res);
		
		String accessToken = responseJson.getString("access_token");
		System.out.println("accessToken: " + accessToken);
		return accessToken;
	}
	
	@Test
	public void getCourse() throws InterruptedException, IOException {
		
		PropertyConfigurator.configure("log4j.properties");
		prop = reusableMethods.getData();
		
		RestAssured.baseURI = prop.getProperty("Host_Course");
		
		String response = given().queryParam("access_token", getAccessToken()).
		when().log().all().
		get(resourcesCourseOAuth2.getCourse()).asString();
		
		System.out.println(response);
		
	}

}
