package unicl;

public class Word {
	String word;
	int freq;
	int sum;
	int d;
	double tf;
	double idf;
	double tfidf;
	String fileName;
	
	public Word() {
		word = "";
		freq = 0;
		sum = 0;
		d = 0;
		tf = 0;
		idf = 0;
		tfidf = 0;
		fileName = "";
	}
}
