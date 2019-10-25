package libraryAPI;

public class resources {
	
	public static String deleteBook() {
		String res = "/Library/DeleteBook.php";
		return res;
	}
	
	public static String addBook() {
		String endpoint = "/Library/Addbook.php";
		return endpoint;
	}

}
