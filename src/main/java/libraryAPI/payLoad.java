package libraryAPI;

public class payLoad {
	
	public static String addBook(String isbn, String aisle) {
		String payload = "{\r\n\r\n\"name\":\"Learn Appium Automation with Java\",\r\n\"isbn\":\"" + isbn + "\",\r\n\"aisle\":\"" + aisle + "\",\r\n\"author\":\"John foe\"\r\n}";
		return payload;
	}

}