package tests;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import twitterAPI.resources;
import utils.reusableMethods;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Properties;

//jar patch to work with oauth
//https://mvnrepository.com/artifact/com.github.scribejava/scribejava-core/2.5.3
//https://mvnrepository.com/artifact/com.github.scribejava/scribejava-apis/2.5.3

public class GetTwiteesUser {
	
//	String consumerKey="0XkQ8PeLBZPECCxrKRI8tsAXp";
//	String consumerSecret="9qC1Z0GuSgeVOjiKu0LoM0cHTQrRtXybdrbfpCwynDl2ZNnbVb";
//	String token="1134239641991884800-aIthGFazi9XVfVRJvpVuNhp56LBGKs";
//	String tokenSecret="31Sd78lZKdnaYEcH3QyNbJVR8D6m7nxkpXVzvFU2kHE7P";
	Properties prop;
	
	String id = null;
	
	
	@Test
	public void getLatestTweet() throws IOException {
		
		prop = reusableMethods.getData();
				
		RestAssured.baseURI = prop.getProperty("HOST_Twitter");
		
		Response res = given().auth().oauth(prop.getProperty("TweetConsumerKey"), prop.getProperty("TweetConsumerSecret"),
				prop.getProperty("TweetToken"), prop.getProperty("TweetTokenSecret")).
		queryParam("count", 1).
		when().get(resources.getTwiteesTimeline()).then().extract().response();

		JsonPath responseJson = reusableMethods.rawToJson(res);
		String text = responseJson.get("[0].text");
		String id = responseJson.get("[0].id_str");
		System.out.println(text);
		System.out.println(id);
	}
	
	
	public void createTweet() throws IOException {
		
		prop = reusableMethods.getData();
				
		RestAssured.baseURI = prop.getProperty("HOST_Twitter");
		
		Response res = given().auth().oauth(prop.getProperty("TweetConsumerKey"), prop.getProperty("TweetConsumerSecret"),
				prop.getProperty("TweetToken"), prop.getProperty("TweetTokenSecret")).
		queryParam("status", "Tweeting by Automation 2700").
		when().post(resources.postTwiteesTimeline()).then().extract().response();
		
		JsonPath responseJson = reusableMethods.rawToJson(res);
		System.out.println("Below is the tweet added");
		String text = responseJson.get("text");
		id = responseJson.get("id").toString();
		System.out.println(text);
		System.out.println(id);
		
	}
	
	@Test
	public void createDeleteTweet() throws IOException {
		
		prop = reusableMethods.getData();
		
		//criando tweet
		createTweet();
		
		RestAssured.baseURI = prop.getProperty("HOST_Twitter");
		
		Response res = given().auth().oauth(prop.getProperty("TweetConsumerKey"), prop.getProperty("TweetConsumerSecret"),
				prop.getProperty("TweetToken"), prop.getProperty("TweetTokenSecret")).
		when().post(resources.deleteTwitee(id)).then().extract().response();
		
		JsonPath responseJson = reusableMethods.rawToJson(res);
		System.out.println("Tweet which got deleted with automation is below 2700");
		String text = responseJson.get("text");
		String id = responseJson.get("id_str");
		System.out.println(text);
		System.out.println(id);
	}

}
