package unicl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExceptionDictionary {

	public static String read() throws IOException {
		
		String dictionary = "";
		String line = "";
		
		
		//FileInputStream fis = new FileInputStream("/home/hadoop/workspace/unicl/dic.txt");
		FileInputStream fis = new FileInputStream("/home/hadoop/workspace/unicl2/dic.txt");
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		
		while((line = br.readLine()) != null){
			dictionary += line;
		}
		
		br.close();
		return dictionary;
	}	
}
