package unicl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//하둡 import
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import com.mongodb.BasicDBObject;

public class Crawler {
	
	private HashMap disallowListCache = new HashMap(); // disallow : ~을 허가하지 않다.
	private PrintWriter logFileWriter; // 로그 파일 출력을 위한 것
	private boolean crawling = true; // 로봇의 활동이 진행 중인지 아닌지 나타냄 (나타내는 곳을 Flag라고 하는 것 같음)
	public static int 
	count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, 
	count6 = 0, count7 = 0, count8 = 0, count9 = 0, count10= 0;
	public String dataTitleWordResult = "";
	
	//예외사전
	private String dictionary = "";

	public void crawl(String startUrl, int maxUrls, String site, String DateString) throws IOException {

		//예외사전 초기화
		ExceptionDictionary dic = new ExceptionDictionary();
		dictionary = dic.read();	
		
		HashSet crawledList = new HashSet(); //수집이 끝난 것을 기록하기 위한 것 
		LinkedHashSet toCrawlList = new LinkedHashSet(); //앞으로 수집할 것을 기록하기 위한 것 

		startUrl = removeWwwFromUrl(startUrl);
		
		toCrawlList.add(startUrl); //수집하기 위한 리스트에 사용자가 출발점으로 지정한 사이트를 기록

		while (crawling && toCrawlList.size() > 0) { 
			if (maxUrls != -1) { //최대 url갯수가 설정 되어 있다면 
				if (crawledList.size() == maxUrls) { //수집된 리스트의 갯구와 최대 url수가 같을때 중지!!
					break;
				}
			}
			
			/*
			if(count1==10)
				break;
			if(count2==10)
				break;
			if(count3==10)
				break;
			if(count4==10)
				break;
			if(count5==10)
				break;
			if(count6==10)
				break;
			if(count7==10)
				break;
			if(count8==10)
				break;
			if(count9==10)
				break;
			if(count10==10)
				break;*/

			// 1번 - 다음 url로 진행
			String url = (String) toCrawlList.iterator().next();
			toCrawlList.remove(url); 

			// 2번 - 크롤링 허용 사이트인지 검사
			URL verifiedUrl = verifyUrl(url); 
			/*
			if (!isRobotAllowed(verifiedUrl)) {
				System.out.println("걸렸다");
				continue;
			}*/

			// 4번
			crawledList.add(url);
			/*
			// 5번
			if(site.equals("chosun")){
				if( !chosun(url) ) ; //continue;
			}
			if(site.equals("khan")){
				if( !khan(url, DateString) ) ; //continue;
			}
			if(site.equals("yonhap")){
				if( !yonhap(url, DateString) ) ; //continue;
			}*/

			// 조선일보를 매개변수로 넘겨받은 스레드일 경우
			if(site.equals("chosun")){ 
				if( !chosun(url, DateString) ) ; //continue;
			}
			// 경향신문을 매개변수로 넘겨받은 스레드일 경우
			else if(site.equals("khan")){ 
				if( !khan(url, DateString) ) ; //continue;
			}
			// 연합뉴스를 매개변수로 넘겨받은 스레드일 경우
			else if(site.equals("yonhap")){ 
				if( !yonhap(url, DateString) ) ; //continue;
			}
			// 한국경제를 매개변수로 넘겨받은 스레드일 경우
			else if(site.equals("hankyung")){ 
				if( !hankyung(url, DateString) ) ; //continue;
			}
			// 국민일보를 매개변수로 넘겨받은 스레드일 경우
			else if(site.equals("kmib")){ 
				if( !kmib(url, DateString) ) ; //continue;
			}				
			// 한국일보를 매개변수로 넘겨받은 스레드일 경우
			else if(site.equals("hankook")){ 
				if( !hankook(url, DateString) ) ; //continue;
			}				
			// 한겨례신문을 매개변수로 넘겨받은 스레드일 경우
			else if(site.equals("hani")){
				if( !hani(url, DateString) ) ; //continue;
			}	
			// 이데일리를 매개변수로 넘겨받은 스레드일 경우
			else if(site.equals("edaily")){ 
				if( !edaily(url, DateString) ) ; //continue;
			}
			// 데일리안을 매개변수로 넘겨받은 스레드일 경우
			else if(site.equals("dailian")){ 
				if( !dailian(url, DateString) ) ; //continue;
			}
			// 서울신문을 매개변수로 넘겨받은 스레드일 경우
			else if(site.equals("seoul")){ 
				if( !seoul(url, DateString) ) ; //continue;
			}				
			
			
			String pageContents = downloadPage(verifiedUrl);
			
			if (pageContents != null && pageContents.length() > 0) {
				// 6번
				ArrayList<String> links = retrieveLinks(verifiedUrl, pageContents, crawledList, true);

				// 7번
				toCrawlList.addAll(links);
			}
		}
	}
	
	private URL verifyUrl(String url) {
		// Only allow HTTP URLs.
		if (!url.toLowerCase().startsWith("http://"))
			return null;

		// Verify format of URL.
		URL verifiedUrl = null;
		try {
			verifiedUrl = new URL(url);
		} catch (Exception e) {
			return null;
		}

		return verifiedUrl;
	}

	@SuppressWarnings("unchecked") //로봇 프로토콜을 이행하기 위한 메서드 
	private boolean isRobotAllowed(URL urlToCheck) {
		String host = urlToCheck.getHost().toLowerCase();

		// Retrieve host's disallow list from cache. //로봇 프로토콜에 맞지 않는 것을 담을 리스트 
		ArrayList<String> disallowList = (ArrayList<String>) disallowListCache.get(host);

		// If list is not in the cache, download and cache it.
		if (disallowList == null) {
			disallowList = new ArrayList();

			try {
				URL robotsFileUrl = new URL("http://" + host + "/robots.txt");

				// Open connection to robot file URL for reading.
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(robotsFileUrl.openStream()));

				// Read robot file, creating list of disallowed paths.
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.indexOf("Disallow:") == 0) {
						String disallowPath = line.substring("Disallow:"
								.length());

						// Check disallow path for comments and remove if
						// present.
						int commentIndex = disallowPath.indexOf("#");
						if (commentIndex != -1) {
							disallowPath = disallowPath.substring(0,
									commentIndex);
						}

						// Remove leading or trailing spaces from disallow path.
						disallowPath = disallowPath.trim();

						// Add disallow path to list.
						disallowList.add(disallowPath);
					}
				}

				// Add new disallow list to cache.
				disallowListCache.put(host, disallowList);
			} catch (Exception e) {
				/*
				 * Assume robot is allowed since an exception is thrown if the
				 * robot file doesn't exist.
				 */
				return true;
			}
		}

		/*
		 * Loop through disallow list to see if the crawling is allowed for the
		 * given URL.
		 */
		String file = urlToCheck.getFile();
		for (int i = 0; i < disallowList.size(); i++) {
			String disallow = (String) disallowList.get(i);
			if (file.startsWith(disallow)) {
				return false;
			}
		}

		return true;
	}

	private String downloadPage(URL pageUrl) {
		try {
			// Open connection to URL for reading.
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					pageUrl.openStream()));

			// Read page into buffer.
			String line;
			StringBuffer pageBuffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				pageBuffer.append(line);
			}

			return pageBuffer.toString();
		} catch (Exception e) {
		}

		return null;
	}

	private ArrayList<String> retrieveLinks(URL pageUrl, String pageContents, HashSet crawledList, boolean limitHost) {
		// Compile link matching pattern.
		Pattern p = Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]", //<a href=""> 이런 것을 걸러냄
				Pattern.CASE_INSENSITIVE); //String이 컴파일될때 패턴을 지정 -> JAVA의 정규표현식 
		Matcher m = p.matcher(pageContents); //정규표현식에 맞는지 매칭시켜본다. 

		// Create list of link matches.
		ArrayList<String> linkList = new ArrayList<String>();
		while (m.find()) { //매치가 되면 계속 돌아간다. 
			String link = m.group(1).trim(); // s.substring(m.start(1), m.end(1))와 같은 표현 
											 // 매칭된 것  중 첫번째에서 link만  잘라내어 저장 
			// Skip empty links. //링크가 비어있다면 
			if (link.length() < 1) {
				continue;
			}

			// Skip links that are just page anchors.
			if (link.charAt(0) == '#') { //#을 쓴 링크라면 
				continue;
			}

			// Skip mailto links.
			if (link.indexOf("mailto:") != -1) {//메일이 link되어 있다면 
				continue;
			}

			// Skip JavaScript links. //javascript가 link 되어 있다면 
			if (link.toLowerCase().indexOf("javascript") != -1) {
				continue;
			}

			// Prefix absolute and relative URLs if necessary.
			if (link.indexOf("://") == -1) { // 이런 형식(://)이 없다면 
				// Handle absolute URLs.
				if (link.charAt(0) == '/') { // 슬래쉬(/)로 시작하는 절대 url일 경우 (예) /blog/hagi
					link = "http://" + pageUrl.getHost() + link; //앞에 http://를 붙인다. 
					// Handle relative URLs.
				} else { //상대 경로 
					String file = pageUrl.getFile();
					if (file.indexOf('/') == -1) { /* index.html과 같은 경우 (슬래쉬가 완전 없는 경우)*/ 
						link = "http://" + pageUrl.getHost() + "/" + link;
					} else { /* blog/hagi 같이 슬래쉬(/)를 쓰지 않은 경우  */
						String path = file.substring(0,
								file.lastIndexOf('/') + 1);
						link = "http://" + pageUrl.getHost() + path + link;
					}
				}
			}

			// Remove anchors from link. //link에 #이 있다면 그전까지 자른다. 
			int index = link.indexOf('#');
			if (index != -1) {
				link = link.substring(0, index);
			}

			// Remove leading "www" from URL's host if present.
			link = removeWwwFromUrl(link); //www를 잘라냄 

			// Verify link and skip if invalid.
			URL verifiedLink = verifyUrl(link);
			if (verifiedLink == null) {
				continue;
			}

			/*
			 * If specified, limit links to those having the same host as the
			 * start URL.
			 *    나의 로봇 시작 페이지가 http://daum.net이고  
			 */ //abc.html의 상위주소가 http://daum.net이라면 이것은 걸러낸다. 
			if (limitHost
					&& !pageUrl.getHost().toLowerCase().equals(
							verifiedLink.getHost().toLowerCase())) {
				continue;
			}

			// Skip link if it has already been crawled.
			if (crawledList.contains(link)) { //이미 crawledList에서 수집된 정보라면 넘어간다. 
				continue;
			}

			// Add link to list.
			linkList.add(link); //모두 아니라면 linkList에 저장 
		}

		return (linkList);
	}

	private boolean searchStringMatches(String pageContents, String searchString, boolean caseSensitive) {
		String searchContents = pageContents;

		/*
		 * If case sensitive search, lowercase page contents for comparison.
		 */
		if (!caseSensitive) { //대소문자를 구분 안할때 
			searchContents = pageContents.toLowerCase();
		}

		// Split search string into individual terms.
		Pattern p = Pattern.compile("[\\s]+"); //공백일때를 기준으로 패턴을 준다. 
		String[] terms = p.split(searchString); //공백을 기준으로 자른다. 

		// Check to see if each term matches.
		for (int i = 0; i < terms.length; i++) { //페이지 내용에 찾을 단어가 있는지 판단하여 리턴 
			if (caseSensitive) {
				if (searchContents.indexOf(terms[i]) == -1) {
					return false;
				}
			} else {
				if (searchContents.indexOf(terms[i].toLowerCase()) == -1) {
					return false;
				}
			}
		}

		return true;
	}

	private String removeWwwFromUrl(String url) {
		int index = url.indexOf("://www.");
		if (index != -1) {
			return url.substring(0, index + 3) + url.substring(index + 7);
		} //(예) http://www.daum.net=> http://daum.net
		return (url);
	}

	private String exceptionDictionary(String data) {
		//예외처리
		data = data.replace("\"", "");
		data = data.replace("-", "");
		data = data.replace("|", "");
		data = data.replace("'", "");
		data = data.replace(",", "");
		data = data.replace("(종합)", "");
		data = data.replace("(종합2보)", "");
		data = data.replace("한국일보", "");
		data = data.replace("한겨레", "");
		data = data.replace("정치일반", "");
		data = data.replace("정치", "");
		data = data.replace("뉴스", "");
		data = data.replace("연합", "");
		data = data.replace("…", " ");
		data = data.replace("ㆍ", " ");
		data = data.replace("‘", "");
		data = data.replace("“", "");
		data = data.replace("·", " ");
		data = data.replace("‘", "");
		data = data.replace("’", "");
		data = data.replace("", "");
		data = data.replace("”", "");
		data = data.replace("(", " ");
		data = data.replace(")", " ");
		data = data.replace("“", "");
		data = data.replace("‘", "");
		data = data.replace(":", "");
		
		return data;
	}
	
	private boolean chosun(String url, String DateString) throws IOException{
		
		if(url.contains("/site/data/html_dir/2015/05/")){
			
			Document doc = Jsoup.connect(url).get();
			
			String selector1 = "div.title_author_2011 h2#title_text";
			String selector2 = "div.article";
			String selector3 = "div.normal_line";
			String selector4 = "div.art_title_2011 dl dt a";
			
			Elements els4 = doc.select(selector4);
			if(!els4.text().toString().equals("정치")) return false;
			
			Elements els1 = doc.select(selector1);
			Elements els2 = doc.select(selector2);
			Elements els3 = doc.select(selector3);

			String dataTitle_original = doc.title();
			String dataBody_original =  els2.text();
			
			String dataTitle_word = "";
			String dataBody_word = "";
			
			//예외처리
			dataTitle_original = exceptionDictionary(dataTitle_original);
			dataBody_original = exceptionDictionary(dataBody_original);	
			
			// 1. 형태소 분석
			MorphemeAnalyzer morphoneAnalyzer = new MorphemeAnalyzer();
			// 기사 제목 형태소 분석
			dataTitle_word = morphoneAnalyzer.wordSplit(dataTitle_original, dictionary);
			// 기사 본문 형태소 분석
			dataBody_word = morphoneAnalyzer.wordSplit(dataBody_original, dictionary);
			
			// 2. 파일 생성
			count1++;
			System.out.println("조선" + count1 + " " + url);
			
			//하둡 파일 저장
			HdfsFileSave hdfsSave = new HdfsFileSave();
			
			//제목 크롤러
			String strPath = "./" + DateString + "/title/chosun/crawler/" + DateString + "_" + count1;
			hdfsSave.hdfsSave(strPath, dataTitle_original);
			
			//내용 크롤러
			strPath = "./" + DateString + "/content/chosun/crawler/" + DateString + "_" + count1;
			hdfsSave.hdfsSave(strPath, dataBody_original);
			
			//제목 한나눔
			strPath = "./" + DateString + "/title/chosun/hannanum/" + DateString + "_" + count1;
			hdfsSave.hdfsSave(strPath, dataTitle_word);			
			
			//내용 한나눔
			strPath = "./" + DateString + "/content/chosun/hannanum/" + DateString + "_" + count1;
			hdfsSave.hdfsSave(strPath, dataBody_word);			
			
			String pathIn = "./" + DateString + "/content/chosun/hannanum/" + DateString + "_" + count1;
			String pathOut = "./" + DateString + "/content/chosun/wordcount/" + DateString + "_" + count1;
			
			//내용 워드카운트
			WordCount wc = new WordCount();
			try {
				wc.start(pathIn, pathOut, "chosun");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			//몽고 디비 객체생성
			MongodbConn db = new MongodbConn();
			try{
			BasicDBObject dbobj = new BasicDBObject("code", DateString + "_" + count1)
				.append("news", "chosun")
				.append("title", dataTitle_original)
				.append("content", dataBody_original)
				.append("url", url);
			
			db.col1.insert(dbobj);
			db.Log(DateString+ "_" + count1, "chosun");
			//db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
			dataTitleWordResult = dataTitleWordResult + dataTitle_word;		
			
		}
		
		return true;
	}
	
	private boolean khan(String url, String DateString) throws IOException{
		if(url.contains("/kh_news/khan_art")){
			
			Document doc = Jsoup.connect(url).get();
			
			String selector1 = "div#sub_cntTop span.article_txt";
			String selector2 = "div#header dl#dept3_one dt a";
			
			Elements els2 = doc.select(selector2);
			if(!els2.text().toString().equals("뉴스:정치")) return false;
			
			Elements els1 = doc.select(selector1);

			String dataTitle_original = doc.title();
			String dataBody_original =  els1.text();
			
			String dataTitle_word = "";
			String dataBody_word = "";
			
			//TF-IDF
			String dataTitle_tfidf = "";
			String dataBody_tfidf = "";
			
			//예외처리
			dataTitle_original = exceptionDictionary(dataTitle_original);
			dataBody_original = exceptionDictionary(dataBody_original);
			
			// 1. 형태소 분석
			MorphemeAnalyzer morphoneAnalyzer = new MorphemeAnalyzer();
			// 기사 제목 형태소 분석
			dataTitle_word = morphoneAnalyzer.wordSplit(dataTitle_original, dictionary);
			// 기사 본문 형태소 분석
			dataBody_word = morphoneAnalyzer.wordSplit(dataBody_original, dictionary);
			
			// 2. 파일 생성
			count2++;
			System.out.println("경향" + count2 + " " + url);
			
			//하둡 파일 저장
			HdfsFileSave hdfsSave = new HdfsFileSave();
			
			//제목 크롤러
			String strPath = "./" + DateString + "/title/khan/crawler/" + DateString + "_" + count2;
			hdfsSave.hdfsSave(strPath, dataTitle_original);
			
			//내용 크롤러
			strPath = "./" + DateString + "/content/khan/crawler/" + DateString + "_" + count2;
			hdfsSave.hdfsSave(strPath, dataBody_original);
			
			//제목 한나눔
			strPath = "./" + DateString + "/title/khan/hannanum/" + DateString + "_" + count2;
			hdfsSave.hdfsSave(strPath, dataTitle_word);			
			
			//내용 한나눔
			strPath = "./" + DateString + "/content/khan/hannanum/" + DateString + "_" + count2;
			hdfsSave.hdfsSave(strPath, dataBody_word);			
			
			String pathIn = "./" + DateString + "/content/khan/hannanum/" + DateString + "_" + count2;
			String pathOut = "./" + DateString + "/content/khan/wordcount/" + DateString + "_" + count2;
			
			//내용 워드카운트
			WordCount wc = new WordCount();
			try {
				wc.start(pathIn, pathOut, "khan");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			//몽고 디비 객체생성
			MongodbConn db = new MongodbConn();
			try{
			BasicDBObject dbobj = new BasicDBObject("code", DateString + "_" + count2)
				.append("news", "khan")
				.append("title", dataTitle_original)
				.append("content", dataBody_original)
				.append("url", url);
			
			db.col1.insert(dbobj);
			db.Log(DateString+ "_" + count2, "khan");
			//db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}				
			
			dataTitleWordResult = dataTitleWordResult + dataTitle_word;					
			
		}
		
		return true;
	}
	
	private boolean yonhap(String url, String DateString) throws IOException{
		
		if(url.contains("politics/2015/05/")){			
							
			Document doc = Jsoup.connect(url).get();
			
			String selector1 = "div.article";
			String selector2 = "div.article";
			
			Elements els1 = doc.select(selector1);
			
			//크롤러 원본
			String dataTitle_original = doc.title();
			String dataBody_original =  els1.text();
			
			//한나눔
			String dataTitle_word = "";
			String dataBody_word = "";
			
			//TF-IDF
			String dataTitle_tfidf = "";
			String dataBody_tfidf = "";
			
			//예외처리
			dataTitle_original = exceptionDictionary(dataTitle_original);
			dataBody_original = exceptionDictionary(dataBody_original);
			
			// 1. 형태소 분석
			MorphemeAnalyzer morphoneAnalyzer = new MorphemeAnalyzer();
			// 기사 제목 형태소 분석
			dataTitle_word = morphoneAnalyzer.wordSplit(dataTitle_original, dictionary);
			// 기사 본문 형태소 분석
			dataBody_word = morphoneAnalyzer.wordSplit(dataBody_original, dictionary);				
			
			/*
			while((dataBody_original.contains("<") && dataBody_original.contains(">"))){
			    dataBody_original = dataBody_original.replace(

			                 dataBody_original.substring(dataBody_original.indexOf("<") , dataBody_original.indexOf(">")+1)   ,  ""   );
			}*/

			
			// 2. 파일 생성
			count3++;
			System.out.println("연합" + count3 + " " + url);
			
			//하둡 파일 저장
			HdfsFileSave hdfsSave = new HdfsFileSave();
			
			//제목 크롤러
			String strPath = "./" + DateString + "/title/yonhap/crawler/" + DateString + "_" + count3;
			hdfsSave.hdfsSave(strPath, dataTitle_original);
			
			//내용 크롤러
			strPath = "./" + DateString + "/content/yonhap/crawler/" + DateString + "_" + count3;
			hdfsSave.hdfsSave(strPath, dataBody_original);
			
			//제목 한나눔
			strPath = "./" + DateString + "/title/yonhap/hannanum/" + DateString + "_" + count3;
			hdfsSave.hdfsSave(strPath, dataTitle_word);			
			
			//내용 한나눔
			strPath = "./" + DateString + "/content/yonhap/hannanum/" + DateString + "_" + count3;
			hdfsSave.hdfsSave(strPath, dataBody_word);								
			
			String pathIn = "./" + DateString + "/content/yonhap/hannanum/" + DateString + "_" + count3;
			String pathOut = "./" + DateString + "/content/yonhap/wordcount/" + DateString + "_" + count3;
			
			
			//내용 워드카운트
			WordCount wc = new WordCount();
			try {
				wc.start(pathIn, pathOut, "yonhap");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//몽고 디비 객체생성
			MongodbConn db = new MongodbConn();
			try{
			BasicDBObject dbobj = new BasicDBObject("code", DateString + "_" + count3)
				.append("news", "yonhap")
				.append("title", dataTitle_original)
				.append("content", dataBody_original)
				.append("url", url);
			
			db.col1.insert(dbobj);
			db.Log(DateString+ "_" + count3, "yonhap");
			//db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			dataTitleWordResult = dataTitleWordResult + dataTitle_word;			
		}
		
		return true;
	}
	
	private boolean hankyung(String url, String DateString) throws IOException{
		
		if(url.contains("news/app/newsview.php?aid=201505")){
			
			Document doc = Jsoup.connect(url).get();
			
			String selector1 = "div#contents div.nav_location";
			String selector2 = "div#newsView";
			
			Elements els1 = doc.select(selector1);
			Elements els2 = doc.select(selector2);
			
			if(!els1.html().contains("정치")) return false;

			String dataTitle_original = doc.title();
			String dataBody_original =  els2.text();
			
			String dataTitle_word = "";
			String dataBody_word = "";
			
			//TF-IDF
			String dataTitle_tfidf = "";
			String dataBody_tfidf = "";
			
			//예외처리
			dataTitle_original = exceptionDictionary(dataTitle_original);
			dataBody_original = exceptionDictionary(dataBody_original);
			
			// 1. 형태소 분석
			MorphemeAnalyzer morphoneAnalyzer = new MorphemeAnalyzer();
			// 기사 제목 형태소 분석
			dataTitle_word = morphoneAnalyzer.wordSplit(dataTitle_original, dictionary);
			// 기사 본문 형태소 분석
			dataBody_word = morphoneAnalyzer.wordSplit(dataBody_original, dictionary);
			
			
			
			// 2. 파일 생성
			count4++;
			System.out.println("한국경제" + count4 + " " + url);
			
			//하둡 파일 저장
			HdfsFileSave hdfsSave = new HdfsFileSave();
			
			//제목 크롤러
			String strPath = "./" + DateString + "/title/hankyung/crawler/" + DateString + "_" + count4;
			hdfsSave.hdfsSave(strPath, dataTitle_original);
			
			//내용 크롤러
			strPath = "./" + DateString + "/content/hankyung/crawler/" + DateString + "_" + count4;
			hdfsSave.hdfsSave(strPath, dataBody_original);
			
			//제목 한나눔
			strPath = "./" + DateString + "/title/hankyung/hannanum/" + DateString + "_" + count4;
			hdfsSave.hdfsSave(strPath, dataTitle_word);			
			
			//내용 한나눔
			strPath = "./" + DateString + "/content/hankyung/hannanum/" + DateString + "_" + count4;
			hdfsSave.hdfsSave(strPath, dataBody_word);			
			
			String pathIn = "./" + DateString + "/content/hankyung/hannanum/" + DateString + "_" + count4;
			String pathOut = "./" + DateString + "/content/hankyung/wordcount/" + DateString + "_" + count4;
			
			
			//내용 워드카운트
			WordCount wc = new WordCount();
			try {
				wc.start(pathIn, pathOut, "hankyung");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//몽고 디비 객체생성
			MongodbConn db = new MongodbConn();
			try{
			BasicDBObject dbobj = new BasicDBObject("code", DateString + "_" + count4)
				.append("news", "hankyung")
				.append("title", dataTitle_original)
				.append("content", dataBody_original)
				.append("url", url);
			
			db.col1.insert(dbobj);
			db.Log(DateString+ "_" + count4, "hankyung");
			//db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			dataTitleWordResult = dataTitleWordResult + dataTitle_word;		
		}
		
		return true;
	}
	
	private boolean kmib(String url, String DateString) throws IOException{
		
		if(url.contains("article")){
			Document doc = Jsoup.connect(url).get();
			
			String selector1 = "div#menu ul.news li.on dl.submenu news_b";
			String selector2 = "span.t11";
			String selector3 = "div#articleBody";
			
			Elements els1 = doc.select(selector1);
			Elements els2 = doc.select(selector2);
			Elements els3 = doc.select(selector3);
			
			
			if(!els1.text().contains("정치")) return false;
			if(!els2.text().contains("2015-05")) return false;
			

			String dataTitle_original = doc.title();
			String dataBody_original =  els3.text();
			
			String dataTitle_word = "";
			String dataBody_word = "";
			
			//TF-IDF
			String dataTitle_tfidf = "";
			String dataBody_tfidf = "";
			
			//예외처리
			dataTitle_original = exceptionDictionary(dataTitle_original);
			dataBody_original = exceptionDictionary(dataBody_original);
			
			// 1. 형태소 분석
			MorphemeAnalyzer morphoneAnalyzer = new MorphemeAnalyzer();
			// 기사 제목 형태소 분석
			dataTitle_word = morphoneAnalyzer.wordSplit(dataTitle_original, dictionary);
			// 기사 본문 형태소 분석
			dataBody_word = morphoneAnalyzer.wordSplit(dataBody_original, dictionary);
			
			
			
			// 2. 파일 생성
			count5++;
			System.out.println("국민" + count5 + " " + url);
			
			//하둡 파일 저장
			HdfsFileSave hdfsSave = new HdfsFileSave();
			
			//제목 크롤러
			String strPath = "./" + DateString + "/title/kmib/crawler/" + DateString + "_" + count5;
			hdfsSave.hdfsSave(strPath, dataTitle_original);
			
			//내용 크롤러
			strPath = "./" + DateString + "/content/kmib/crawler/" + DateString + "_" + count5;
			hdfsSave.hdfsSave(strPath, dataBody_original);
			
			//제목 한나눔
			strPath = "./" + DateString + "/title/kmib/hannanum/" + DateString + "_" + count5;
			hdfsSave.hdfsSave(strPath, dataTitle_word);			
			
			//내용 한나눔
			strPath = "./" + DateString + "/content/kmib/hannanum/" + DateString + "_" + count5;
			hdfsSave.hdfsSave(strPath, dataBody_word);			
						
			String pathIn = "./" + DateString + "/content/kmib/hannanum/" + DateString + "_" + count5;
			String pathOut = "./" + DateString + "/content/kmib/wordcount/" + DateString + "_" + count5;
			
			//내용 워드카운트
			WordCount wc = new WordCount();
			try {
				wc.start(pathIn, pathOut, "kmib");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//몽고 디비 객체생성
			MongodbConn db = new MongodbConn();
			try{
			BasicDBObject dbobj = new BasicDBObject("code", DateString + "_" + count5)
				.append("news", "kmib")
				.append("title", dataTitle_original)
				.append("content", dataBody_original)
				.append("url", url);
			
			db.col1.insert(dbobj);
			db.Log(DateString+ "_" + count5, "kmib");
			//db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			dataTitleWordResult = dataTitleWordResult + dataTitle_word;			
		}
		
		return true;
	}	
	
	private boolean hankook(String url, String DateString) throws IOException{
		
		if(url.contains("hankookilbo.com/v")){
			Document doc = Jsoup.connect(url).get();
			
			String selector1 = "div#menu-wrap a.selected";
			String selector2 = "div#date-registered";
			String selector3 = "div#article-body";
			
			Elements els1 = doc.select(selector1);
			Elements els2 = doc.select(selector2);
			Elements els3 = doc.select(selector3);

			if(!els1.text().contains("정치")) return false;
			if(!els2.text().contains("2015.05")) return false;	
			
			String dataTitle_original = doc.title();
			String dataBody_original =  els3.text();
			
			String dataTitle_word = "";
			String dataBody_word = "";
			
			//TF-IDF
			String dataTitle_tfidf = "";
			String dataBody_tfidf = "";
			
			//예외처리
			dataTitle_original = exceptionDictionary(dataTitle_original);
			dataBody_original = exceptionDictionary(dataBody_original);
			
			// 1. 형태소 분석
			MorphemeAnalyzer morphoneAnalyzer = new MorphemeAnalyzer();
			// 기사 제목 형태소 분석
			dataTitle_word = morphoneAnalyzer.wordSplit(dataTitle_original, dictionary);
			// 기사 본문 형태소 분석
			dataBody_word = morphoneAnalyzer.wordSplit(dataBody_original, dictionary);
			
			
			
			// 2. 파일 생성
			count6++;
			System.out.println("한국" + count6 + " " + url);
			
			//하둡 파일 저장
			HdfsFileSave hdfsSave = new HdfsFileSave();
			
			//제목 크롤러
			String strPath = "./" + DateString + "/title/hankook/crawler/" + DateString + "_" + count6;
			hdfsSave.hdfsSave(strPath, dataTitle_original);
			
			//내용 크롤러
			strPath = "./" + DateString + "/content/hankook/crawler/" + DateString + "_" + count6;
			hdfsSave.hdfsSave(strPath, dataBody_original);
			
			//제목 한나눔
			strPath = "./" + DateString + "/title/hankook/hannanum/" + DateString + "_" + count6;
			hdfsSave.hdfsSave(strPath, dataTitle_word);			
			
			//내용 한나눔
			strPath = "./" + DateString + "/content/hankook/hannanum/" + DateString + "_" + count6;
			hdfsSave.hdfsSave(strPath, dataBody_word);			
			
			String pathIn = "./" + DateString + "/content/hankook/hannanum/" + DateString + "_" + count6;
			String pathOut = "./" + DateString + "/content/hankook/wordcount/" + DateString + "_" + count6;
			
			//내용 워드카운트
			WordCount wc = new WordCount();
			try {
				wc.start(pathIn, pathOut, "hankook");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//몽고 디비 객체생성
			MongodbConn db = new MongodbConn();
			try{
			BasicDBObject dbobj = new BasicDBObject("code", DateString + "_" + count6)
				.append("news", "hankook")
				.append("title", dataTitle_original)
				.append("content", dataBody_original)
				.append("url", url);
			
			db.col1.insert(dbobj);
			db.Log(DateString+ "_" + count6, "hankook");
			//db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
			dataTitleWordResult = dataTitleWordResult + dataTitle_word;			
		}
		
		return true;
	}		
	
	private boolean hani(String url, String DateString) throws IOException{
		
		if(url.contains("/arti/politics/")){
			Document doc = Jsoup.connect(url).get();
			
			String selector1 = "div.article-head p.date-time";
			String selector2 = "div.text";
			
			Elements els1 = doc.select(selector1);
			Elements els2 = doc.select(selector2);

			if(!els1.text().contains("2015-05")) return false;
			
			String dataTitle_original = doc.title();
			String dataBody_original =  els2.text();
			
			String dataTitle_word = "";
			String dataBody_word = "";
			
			//TF-IDF
			String dataTitle_tfidf = "";
			String dataBody_tfidf = "";
			
			//예외처리
			dataTitle_original = exceptionDictionary(dataTitle_original);
			dataBody_original = exceptionDictionary(dataBody_original);
			
			// 1. 형태소 분석
			MorphemeAnalyzer morphoneAnalyzer = new MorphemeAnalyzer();
			// 기사 제목 형태소 분석
			dataTitle_word = morphoneAnalyzer.wordSplit(dataTitle_original, dictionary);
			// 기사 본문 형태소 분석
			dataBody_word = morphoneAnalyzer.wordSplit(dataBody_original, dictionary);
			
			// 2. 파일 생성
			count7++;
			System.out.println("한겨례" + count7 + " " + url);
			
			//하둡 파일 저장
			HdfsFileSave hdfsSave = new HdfsFileSave();
			
			//제목 크롤러
			String strPath = "./" + DateString + "/title/hani/crawler/" + DateString + "_" + count7;
			hdfsSave.hdfsSave(strPath, dataTitle_original);
			
			//내용 크롤러
			strPath = "./" + DateString + "/content/hani/crawler/" + DateString + "_" + count7;
			hdfsSave.hdfsSave(strPath, dataBody_original);
			
			//제목 한나눔
			strPath = "./" + DateString + "/title/hani/hannanum/" + DateString + "_" + count7;
			hdfsSave.hdfsSave(strPath, dataTitle_word);			
			
			//내용 한나눔
			strPath = "./" + DateString + "/content/hani/hannanum/" + DateString + "_" + count7;
			hdfsSave.hdfsSave(strPath, dataBody_word);			
			
			String pathIn = "./" + DateString + "/content/hani/hannanum/" + DateString + "_" + count7;
			String pathOut = "./" + DateString + "/content/hani/wordcount/" + DateString + "_" + count7;
			
			//내용 워드카운트
			WordCount wc = new WordCount();
			try {
				wc.start(pathIn, pathOut, "hani");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//몽고 디비 객체생성
			MongodbConn db = new MongodbConn();
			try{
			BasicDBObject dbobj = new BasicDBObject("code", DateString + "_" + count7)
				.append("news", "hani")
				.append("title", dataTitle_original)
				.append("content", dataBody_original)
				.append("url", url);
			
			db.col1.insert(dbobj);
			db.Log(DateString+ "_" + count7, "hani");
			//db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}					
			
			dataTitleWordResult = dataTitleWordResult + dataTitle_word;		
		}
		
		return true;
	}		
	
	private boolean edaily(String url, String DateString) throws IOException{
		
		if(url.contains("/news/NewsRead")){
			Document doc = Jsoup.connect(url).get();
			
			String selector1 = "div#submenu ul li.select";
			String selector2 = "div#viewarea div.pr p.newsdate";
			String selector3 = "span#viewcontent_inner";
			
			Elements els1 = doc.select(selector1);
			Elements els2 = doc.select(selector2);
			Elements els3 = doc.select(selector3);

			
			if(!els1.text().contains("정치")) return false;
			if(!els2.text().contains("2015.05")) return false;
			
			String dataTitle_original = doc.title();
			String dataBody_original =  els3.text();
			
			String dataTitle_word = "";
			String dataBody_word = "";
			
			//TF-IDF
			String dataTitle_tfidf = "";
			String dataBody_tfidf = "";
			
			//예외처리
			dataTitle_original = exceptionDictionary(dataTitle_original);
			dataBody_original = exceptionDictionary(dataBody_original);
			
			// 1. 형태소 분석
			MorphemeAnalyzer morphoneAnalyzer = new MorphemeAnalyzer();
			// 기사 제목 형태소 분석
			dataTitle_word = morphoneAnalyzer.wordSplit(dataTitle_original, dictionary);
			// 기사 본문 형태소 분석
			dataBody_word = morphoneAnalyzer.wordSplit(dataBody_original, dictionary);
			
			
			
			// 2. 파일 생성
			count8++;
			System.out.println("이데일리" + count8 + " " + url);
			
			//하둡 파일 저장
			HdfsFileSave hdfsSave = new HdfsFileSave();
			
			//제목 크롤러
			String strPath = "./crawler/edaily/title/" + DateString + "_" + count8;
			hdfsSave.hdfsSave(strPath, dataTitle_original);
			
			//내용 크롤러
			strPath = "./crawler/edaily/content/" + DateString + "_" + count8;
			hdfsSave.hdfsSave(strPath, dataBody_original);
			
			//제목 한나눔
			strPath = "./hannanum/edaily/title/" + DateString + "_" + count8;
			hdfsSave.hdfsSave(strPath, dataTitle_word);			
			
			//내용 한나눔
			strPath = "./hannanum/edaily/content/" + DateString + "_" + count8;
			hdfsSave.hdfsSave(strPath, dataBody_word);			
			
			String pathIn = "./" + DateString + "/content/edaily/hannanum/" + DateString + "_" + count8;
			String pathOut = "./" + DateString + "/content/edaily/wordcount/" + DateString + "_" + count8;			
			
			//내용 워드카운트
			WordCount wc = new WordCount();
			try {
				wc.start(pathIn, pathOut, "edaily");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//몽고 디비 객체생성
			MongodbConn db = new MongodbConn();
			try{
			BasicDBObject dbobj = new BasicDBObject("code", DateString + "_" + count8)
				.append("news", "edaily")
				.append("title", dataTitle_original)
				.append("content", dataBody_original)
				.append("url", url);
			
			db.col1.insert(dbobj);
			db.Log(DateString+ "_" + count8, "edaily");
			//db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
			dataTitleWordResult = dataTitleWordResult + dataTitle_word;			
		}
		
		return true;
	}		
	
	private boolean dailian(String url, String DateString) throws IOException{
		
		if(url.contains("/news/view/")){
			Document doc = Jsoup.connect(url).get();
			
			String selector1 = "div#view_titlebox";
			String selector2 = "div#view_titlebox2_3";
			String selector3 = "div#view_con";
			
			Elements els1 = doc.select(selector1);
			Elements els2 = doc.select(selector2);
			Elements els3 = doc.select(selector3);

			
			if(!els1.html().contains("view_titlebox_politics")) return false;
			if(!els2.text().contains("2015-05")) return false;
			
			
			String dataTitle_original = doc.title();
			String dataBody_original =  els3.text();
			
			String dataTitle_word = "";
			String dataBody_word = "";
			
			//TF-IDF
			String dataTitle_tfidf = "";
			String dataBody_tfidf = "";
			
			//예외처리
			dataTitle_original = exceptionDictionary(dataTitle_original);
			dataBody_original = exceptionDictionary(dataBody_original);
			
			// 1. 형태소 분석
			MorphemeAnalyzer morphoneAnalyzer = new MorphemeAnalyzer();
			// 기사 제목 형태소 분석
			dataTitle_word = morphoneAnalyzer.wordSplit(dataTitle_original, dictionary);
			// 기사 본문 형태소 분석
			dataBody_word = morphoneAnalyzer.wordSplit(dataBody_original, dictionary);
			
			// 2. 파일 생성
			count9++;
			System.out.println("데일리안" + count9 + " " + url);
			
			//하둡 파일 저장
			HdfsFileSave hdfsSave = new HdfsFileSave();
			
			//제목 크롤러
			String strPath = "./crawler/dailian/title/" + DateString + "_" + count9;
			hdfsSave.hdfsSave(strPath, dataTitle_original);
			
			//내용 크롤러
			strPath = "./crawler/dailian/content/" + DateString + "_" + count9;
			hdfsSave.hdfsSave(strPath, dataBody_original);
			
			//제목 한나눔
			strPath = "./hannanum/dailian/title/" + DateString + "_" + count9;
			hdfsSave.hdfsSave(strPath, dataTitle_word);			
			
			//내용 한나눔
			strPath = "./hannanum/dailian/content/" + DateString + "_" + count9;
			hdfsSave.hdfsSave(strPath, dataBody_word);			
			
			String pathIn = "./" + DateString + "/content/edaily/hannanum/" + DateString + "_" + count8;
			String pathOut = "./" + DateString + "/content/edaily/wordcount/" + DateString + "_" + count8;		
			
			//몽고 디비 객체생성
			MongodbConn db = new MongodbConn();
			try{
			BasicDBObject dbobj = new BasicDBObject("code", DateString + "_" + count9)
				.append("news", "dailian")
				.append("title", dataTitle_original)
				.append("content", dataBody_original)
				.append("url", url);
			
			db.col1.insert(dbobj);
			db.Log(DateString+ "_" + count9, "dailian");
			//db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}				
			
			dataTitleWordResult = dataTitleWordResult + dataTitle_word;			
		}
		
		return true;
	}			
	
	private boolean seoul(String url, String DateString) throws IOException{
		
		if(url.contains("/news/newsView")){
			Document doc = Jsoup.connect(url).get();
			
			String selector1 = "h2.depb_section";
			String selector2 = "div.VCdate";
			String selector3 = "div#atic_txt1";
			
			
			Elements els1 = doc.select(selector1);
			Elements els2 = doc.select(selector2);
			Elements els3 = doc.select(selector3);

			
			if(!els1.html().contains("12_title_h2_politics")) return false;
			if(!els2.text().contains("2015-05")) return false;
			
			
			String dataTitle_original = doc.title();
			String dataBody_original =  els3.text();
			
			String dataTitle_word = "";
			String dataBody_word = "";
			
			//TF-IDF
			String dataTitle_tfidf = "";
			String dataBody_tfidf = "";
			
			//예외처리
			dataTitle_original = exceptionDictionary(dataTitle_original);
			dataBody_original = exceptionDictionary(dataBody_original);
			
			// 1. 형태소 분석
			MorphemeAnalyzer morphoneAnalyzer = new MorphemeAnalyzer();
			// 기사 제목 형태소 분석
			dataTitle_word = morphoneAnalyzer.wordSplit(dataTitle_original, dictionary);
			// 기사 본문 형태소 분석
			dataBody_word = morphoneAnalyzer.wordSplit(dataBody_original, dictionary);
			
			// 2. 파일 생성
			count10++;
			System.out.println("서울" + count10 + " " + url);
			
			//하둡 파일 저장
			HdfsFileSave hdfsSave = new HdfsFileSave();
			
			//제목 크롤러
			String strPath = "./" + DateString + "/title/seoul/crawler/" + DateString + "_" + count10;
			hdfsSave.hdfsSave(strPath, dataTitle_original);
			
			//내용 크롤러
			strPath = "./" + DateString + "/content/seoul/crawler/" + DateString + "_" + count10;
			hdfsSave.hdfsSave(strPath, dataBody_original);
			
			//제목 한나눔
			strPath = "./" + DateString + "/title/seoul/hannanum/" + DateString + "_" + count10;
			hdfsSave.hdfsSave(strPath, dataTitle_word);			
			
			//내용 한나눔
			strPath = "./" + DateString + "/content/seoul/hannanum/" + DateString + "_" + count10;
			hdfsSave.hdfsSave(strPath, dataBody_word);			
			
			String pathIn = "./" + DateString + "/content/seoul/hannanum/" + DateString + "_" + count10;
			String pathOut = "./" + DateString + "/content/seoul/wordcount/" + DateString + "_" + count10;
			
			//내용 워드카운트
			WordCount wc = new WordCount();
			try {
				wc.start(pathIn, pathOut, "seoul");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//몽고 디비 객체생성
			MongodbConn db = new MongodbConn();
			try{
			BasicDBObject dbobj = new BasicDBObject("code", DateString + "_" + count10)
				.append("news", "seoul")
				.append("title", dataTitle_original)
				.append("content", dataBody_original)
				.append("url", url);
			
			db.col1.insert(dbobj);
			db.Log(DateString+ "_" + count10, "seoul");
			//db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}					
			
			dataTitleWordResult = dataTitleWordResult + dataTitle_word;		
		}
		
		return true;
	}			
}