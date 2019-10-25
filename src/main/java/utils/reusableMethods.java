package utils;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeSuite;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class reusableMethods {
	
	public static String issueID = null; 
	final static Logger log = Logger.getLogger(reusableMethods.class);
	static Properties prop = new Properties();
	
	@BeforeSuite
	public void configLog4j() {
		PropertyConfigurator.configure("log4j.properties");	
	}
	
	
	public static XmlPath rawToXML(Response response) {
		//Convert response to string
		String responseString = response.asString();
		log.info(responseString);
		
		//Convert string to XML
		XmlPath responseXml = new XmlPath(responseString);
		return responseXml;
	}
	
	public static JsonPath rawToJson(Response response) {
		//Convert response to string
		String responseString = response.asString();
		log.info("String response: " + responseString);
		
		//Convert string to JSON
		JsonPath responseJson = new JsonPath(responseString);
		return responseJson;
	}
	
	public static String getSessionKey() {
		//Creating session
		RestAssured.baseURI = "http://localhost:8080";
		
		Response resAuth = given().header("Content-Type", "application/json").
		body("{	\"username\": \"albertmvieira\", \"password\": \"helen221109\" }").
		when().
		post("/rest/auth/1/session").then().statusCode(200).
		extract().response();
		
		JsonPath responseAuth = reusableMethods.rawToJson(resAuth);
		String sessionId = responseAuth.get("session.value");
		log.info("session: " + sessionId);
		return sessionId;
	}
	
	//method to create issue
	public static String createIssue() {
		//creating issue/defect
		Response resIssue = given().header("Content-Type", "application/json").
		header("Cookie", "JSESSIONID=" + reusableMethods.getSessionKey()).
		body("{" + 
				"    \"fields\": {" + 
				"       \"project\":{" + 
				"          \"key\": \"API\"" + 
				"       }," + 
				"       \"summary\": \"API Bug 15 10_09_19\"," + 
				"       \"description\": \"Bug criado utilizando RestAssured via REST API\"," + 
				"       \"issuetype\": {" + 
				"          \"name\": \"Bug\"" + 
				"       }" + 
				"   }" + 
				"}").
		when().
		post("/rest/api/2/issue").then().statusCode(201).extract().response();
		
		JsonPath responseIssue = reusableMethods.rawToJson(resIssue);
		issueID = responseIssue.get("id");
		System.out.println("Issue ID: " + issueID);
		return issueID;
	}
	
	//method to create comment, using issue created in method before
	public static String createIssueComment() {
		//creating issue/defect
		String issue = reusableMethods.createIssue();
				
		Response resComment = given().header("Content-Type", "application/json").
		header("Cookie", "JSESSIONID=" + reusableMethods.getSessionKey()).
		body("{\r\n" + 
				"  \"visibility\": {\r\n" + 
				"    \"type\": \"role\",\r\n" + 
				"    \"value\": \"Administrators\"\r\n" + 
				"  },\r\n" + 
				"  \"body\": \"------Albert---------- Comment 5 added by RestAssured Rest-API.\"\r\n" + 
				"}").
		when().
		post("/rest/api/2/issue/" + issue + "/comment").then().statusCode(201).extract().response();
		JsonPath responseComment = reusableMethods.rawToJson(resComment);
		String commentID = responseComment.get("id");
		System.out.println("Comment ID: " + commentID);
		return commentID;
	}
	
	public static Properties getData() throws IOException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\properties\\env.properties");
		prop.load(fis);
		return prop;
	}

}
