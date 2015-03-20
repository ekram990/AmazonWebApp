package ac.il.technion.amazon;

/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
*/
public class Main {

	/**
	 * @param args
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			InputStream in = new URL("http://snap.stanford.edu/data/web-Amazon-links.html").openConnection().getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = reader.readLine();
			System.out.println(line);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Hello World");
	}
*/
}
