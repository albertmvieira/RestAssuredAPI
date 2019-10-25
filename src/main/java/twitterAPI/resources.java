package twitterAPI;

public class resources {
	
	public static String getTwiteesTimeline() {
		String endpoint = "/home_timeline.json";
		return endpoint;
	}
	
	public static String postTwiteesTimeline() {
		String endpoint = "/update.json";
		return endpoint;
	}
	
	public static String deleteTwitee(String id) {
		String endpoint = "destroy/" + id + ".json";
		return endpoint;
	}
	
}
