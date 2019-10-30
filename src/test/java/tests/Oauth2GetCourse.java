package tests;

import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import CourseOAuth2.GetCoursePojo;
import CourseOAuth2.WebAutomationPojo;
import CourseOAuth2.resourcesCourseOAuth2;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utils.reusableMethods;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		
		GetCoursePojo resGetCourse = given().queryParam("access_token", getAccessToken()).
		expect().defaultParser(Parser.JSON). //realizado Parse para Json pois o response header não formata no content-type json, volta como text por isso é necessário para não se perder 
		when().
		get(resourcesCourseOAuth2.getCourse()).as(GetCoursePojo.class);
		
		String expCourseTitleApi = "SoapUI Webservices testing";
		String[] expCourseTitleWeb = {"Selenium Webdriver Java", "Cypress", "Protractor"}; 
		
		//declarando array list que será usando na 2ª forma de validação
		ArrayList<String> listCourseTitWeb = new ArrayList<String>();
		
		
		System.out.println("LinkedIn: " + resGetCourse.getLinkedIn());
		System.out.println("Instructor: " + resGetCourse.getInstructor());
		System.out.println("Title course: " + resGetCourse.getCourses().getApi().get(1).getCourseTitle());
		
		Assert.assertEquals(resGetCourse.getLinkedIn(), "https://www.linkedin.com/in/rahul-shetty-trainer/");
		Assert.assertEquals(resGetCourse.getCourses().getApi().get(1).getCourseTitle(), expCourseTitleApi);
		
		//for para percorrer a lista de array "cursos" e ao encontrar curso, validar o preço.
		for (int i = 0; i < resGetCourse.getCourses().getApi().size(); i++) {
			if (resGetCourse.getCourses().getApi().get(i).getCourseTitle().equals(expCourseTitleApi) ) {
				System.out.println(resGetCourse.getCourses().getApi().get(i).getPrice());
				Assert.assertEquals(resGetCourse.getCourses().getApi().get(i).getPrice(), "40");				
			}
		}
		
		//Validando os cursos da lista de webautomation
		List<WebAutomationPojo> listWebAut = resGetCourse.getCourses().getWebAutomation();
		for (int j = 0; j < listWebAut.size(); j++) {
			System.out.println("WebAutomation course: " + listWebAut.get(j).getCourseTitle());
			
			//A validação pode ser feita de várias maneiras, 
			//1º validando curso a curso tendo a mesma posição, tanto no na lista quanto no array de strings
			Assert.assertEquals(listWebAut.get(j).getCourseTitle(), expCourseTitleWeb[j].trim());
			
			//2º inserindo os valores do titulo do campo dentro de um Arraylist e comparar o conteudo das duas listas.
			listCourseTitWeb.add(listWebAut.get(j).getCourseTitle());		
		}
		
		//Convertendo array simples de String para Arraylist, para facilitar a comparação com o ArrayList de cursos adicionados da API
		List<String> expectedList =  Arrays.asList(expCourseTitleWeb);

		//Comparando o Array list da API com a lista de cursos experada que foi informada no array de string declarado. pode vir do cucumber
		Assert.assertTrue(listCourseTitWeb.equals(expectedList));

	}

}
