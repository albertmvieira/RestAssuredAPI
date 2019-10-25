package googleAPI;

public class resources {
	
	public static String placePostDataJson() {
		String res = "/maps/api/place/add/json";
		return res;
	}
	
	public static String placePostDataXML() {
		String res = "/maps/api/place/add/xml";
		return res;
	}
	
	public static String placeDeleteData() {
		String res = "/maps/api/place/delete/json";
		return res;
	}
	
	public static String getGoogleAPI() {
		String res = "/maps/api/place/findplacefromtext/json";
		return res; 
	}
	
	public static String getPlaceDetail() {
		String endpoint = "maps/api/place/details/json";
		return endpoint;
	}

}
