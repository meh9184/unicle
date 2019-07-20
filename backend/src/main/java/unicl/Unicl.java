package unicl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class Unicl extends Thread {

	public String totalDataTitleWord = "";
	public void run(String newsName){
		
		try {
			  
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String DateString = format.format(now);	
			
			if(newsName.equals("chosun")){
				Crawler crawler = new Crawler();
				crawler.crawl("http://news.chosun.com/politics/index.html?gnb_menu", 1000,"chosun",DateString);
				
				Configuration conf = new Configuration();
				FileSystem hdfs = FileSystem.get(conf);
				
				Path path = new Path("./" + DateString + "/title/chosun/wordcount/" + DateString);
				FSDataOutputStream outStream = hdfs.create(path);

				outStream.writeUTF(crawler.dataTitleWordResult);
				//전체 신문 제목 합치기
				totalDataTitleWord += crawler.dataTitleWordResult;
				outStream.close();
				
				String strPathIn = "./" + DateString + "/title/chosun/wordcount/" + DateString;
				String strPathOut = "./" + DateString + "/title/chosun/wordcount/" + DateString + "_result";					
				
				WordCount wc = new WordCount();
				try {
					//워드카운트
					wc.start(strPathIn, strPathOut, "chosun");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}							
			}
		
			
			else if(newsName.equals("khan")){
				Crawler crawler = new Crawler();
				crawler.crawl("http://news.khan.co.kr/kh_politics/", 1000,"khan",DateString);
				
				Configuration conf = new Configuration();
				FileSystem hdfs = FileSystem.get(conf);
				
				Path path = new Path("./" + DateString + "/title/khan/wordcount/" + DateString);
				FSDataOutputStream outStream = hdfs.create(path);

				outStream.writeUTF(crawler.dataTitleWordResult);
				//전체 신문 제목 합치기
				totalDataTitleWord += crawler.dataTitleWordResult;
				outStream.close();
				
				String strPathIn = "./" + DateString + "/title/chosun/wordcount/" + DateString;
				String strPathOut = "./" + DateString + "/title/chosun/wordcount/" + DateString + "_result";				
				
				WordCount wc = new WordCount();
				try {
					//워드카운트
					wc.start(strPathIn, strPathOut, "khan");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}	
			
			else if(newsName.equals("yonhap")){			
				Crawler crawler = new Crawler();
				crawler.crawl("http://www.yonhapnews.co.kr/politics/index.html?template=5544", 1000,"yonhap",DateString);
				
				Configuration conf = new Configuration();
				FileSystem hdfs = FileSystem.get(conf);
				
				Path path = new Path("./" + DateString + "/title/yonhap/wordcount/" + DateString);
				FSDataOutputStream outStream = hdfs.create(path);

				outStream.writeUTF(crawler.dataTitleWordResult);
				
				//전체 신문 제목 합치기
				totalDataTitleWord += crawler.dataTitleWordResult;
				outStream.close();
				
				String strPathIn = "./" + DateString + "/title/yonhap/wordcount/" + DateString;
				String strPathOut = "./" + DateString + "/title/yonhap/wordcount/" + DateString + "_result";			
				
				//전체 기사 제목 워드카운트
				WordCount wc = new WordCount();
				try {
					//워드카운트
					wc.start(strPathIn, strPathOut, "yonhap");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			else if(newsName.equals("hankyung")){
				Crawler crawler = new Crawler();
				crawler.crawl("http://www.hankyung.com/news/app/newslist.php?sid=010610&nid=010", 1000, "hankyung",DateString);
				
				Configuration conf = new Configuration();
				FileSystem hdfs = FileSystem.get(conf);
				
				Path path = new Path("./" + DateString + "/title/hankyung/wordcount/" + DateString);
				FSDataOutputStream outStream = hdfs.create(path);

				outStream.writeUTF(crawler.dataTitleWordResult);
				//전체 신문 제목 합치기
				totalDataTitleWord += crawler.dataTitleWordResult;
				outStream.close();
				
				String strPathIn = "./" + DateString + "/title/hankyung/wordcount/" + DateString;
				String strPathOut = "./" + DateString + "/title/hankyung/wordcount/" + DateString + "_result";			
				
				//전체 기사 제목 워드카운트
				WordCount wc = new WordCount();
				try {
					//워드카운트
					wc.start(strPathIn, strPathOut, "hankyung");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}			

			else if(newsName.equals("kmib")){
				Crawler crawler = new Crawler();
				crawler.crawl("http://news.kmib.co.kr/article/list.asp?sid1=pol", 1000, "kmib",DateString);
				
				Configuration conf = new Configuration();
				FileSystem hdfs = FileSystem.get(conf);
				
				Path path = new Path("./" + DateString + "/title/kmib/wordcount/" + DateString);
				FSDataOutputStream outStream = hdfs.create(path);

				outStream.writeUTF(crawler.dataTitleWordResult);
				//전체 신문 제목 합치기
				totalDataTitleWord += crawler.dataTitleWordResult;
				outStream.close();
				
				String strPathIn = "./" + DateString + "/title/kmib/wordcount/" + DateString;
				String strPathOut = "./" + DateString + "/title/kmib/wordcount/" + DateString + "_result";							
				
				WordCount wc = new WordCount();
				try {
					//워드카운트
					wc.start(strPathIn, strPathOut, "kmib");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}						
			}					

			else if(newsName.equals("hankook")){
				Crawler crawler = new Crawler();
				crawler.crawl("http://www.hankookilbo.com/l.aspx?c=1", 1000, "hankook",DateString);
				
				Configuration conf = new Configuration();
				FileSystem hdfs = FileSystem.get(conf);
				
				Path path = new Path("./" + DateString + "/title/hankook/wordcount/" + DateString);
				FSDataOutputStream outStream = hdfs.create(path);

				outStream.writeUTF(crawler.dataTitleWordResult);
				//전체 신문 제목 합치기
				totalDataTitleWord += crawler.dataTitleWordResult;
				outStream.close();
				
				String strPathIn = "./" + DateString + "/title/hankook/wordcount/" + DateString;
				String strPathOut = "./" + DateString + "/title/hankook/wordcount/" + DateString + "_result";					
				
				WordCount wc = new WordCount();
				try {
					//워드카운트
					wc.start(strPathIn, strPathOut, "hankook");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}				

			else if(newsName.equals("hani")){
				Crawler crawler = new Crawler();
				crawler.crawl("http://www.hani.co.kr/arti/politics", 1000, "hani",DateString);
				
				Configuration conf = new Configuration();
				FileSystem hdfs = FileSystem.get(conf);
				
				Path path = new Path("./" + DateString + "/title/hani/wordcount/" + DateString);
				FSDataOutputStream outStream = hdfs.create(path);

				outStream.writeUTF(crawler.dataTitleWordResult);
				//전체 신문 제목 합치기
				totalDataTitleWord += crawler.dataTitleWordResult;
				outStream.close();
				
				String strPathIn = "./" + DateString + "/title/hani/wordcount/" + DateString;
				String strPathOut = "./" + DateString + "/title/hani/wordcount/" + DateString + "_result";						
				
				WordCount wc = new WordCount();
				try {
					//워드카운트
					wc.start(strPathIn, strPathOut, "hani");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}				
			
			else if(newsName.equals("edaily")){
				Crawler crawler = new Crawler();
				crawler.crawl("http://www.edaily.co.kr/news/politics/", 1000, "edaily",DateString);
				
				Configuration conf = new Configuration();
				FileSystem hdfs = FileSystem.get(conf);
				
				Path path = new Path("./" + DateString + "/title/edaily/wordcount/" + DateString);
				FSDataOutputStream outStream = hdfs.create(path);

				outStream.writeUTF(crawler.dataTitleWordResult);
				//전체 신문 제목 합치기
				totalDataTitleWord += crawler.dataTitleWordResult;
				outStream.close();
				
				String strPathIn = "./" + DateString + "/title/edaily/wordcount/" + DateString;
				String strPathOut = "./" + DateString + "/title/edaily/wordcount/" + DateString + "_result";				
				
				WordCount wc = new WordCount();
				try {
					//워드카운트
					wc.start(strPathIn, strPathOut, "edaily");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}										
			}				
			
			else if(newsName.equals("dailian")){
				Crawler crawler = new Crawler();
				crawler.crawl("http://www.dailian.co.kr/newslist/?code=1&kind=menu_code", 1000, "dailian",DateString);
				
				Configuration conf = new Configuration();
				FileSystem hdfs = FileSystem.get(conf);
				
				Path path = new Path("./" + DateString + "/title/dailian/wordcount/" + DateString);
				FSDataOutputStream outStream = hdfs.create(path);

				outStream.writeUTF(crawler.dataTitleWordResult);
				//전체 신문 제목 합치기
				totalDataTitleWord += crawler.dataTitleWordResult;
				outStream.close();
				
				String strPathIn = "./" + DateString + "/title/dailian/wordcount/" + DateString;
				String strPathOut = "./" + DateString + "/title/dailian/wordcount/" + DateString + "_result";					
				
				WordCount wc = new WordCount();
				try {
					//워드카운트
					wc.start(strPathIn, strPathOut, "dailian");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}								
			}	
			
			else if(newsName.equals("seoul")){
				Crawler crawler = new Crawler();
				crawler.crawl("http://www.seoul.co.kr/news/newsList.php?section=politics", 1000, "seoul",DateString);
				
				Configuration conf = new Configuration();
				FileSystem hdfs = FileSystem.get(conf);
				
				Path path = new Path("./" + DateString + "/title/seoul/wordcount/" + DateString);
				FSDataOutputStream outStream = hdfs.create(path);

				outStream.writeUTF(crawler.dataTitleWordResult);
				//전체 신문 제목 합치기
				totalDataTitleWord += crawler.dataTitleWordResult;
				outStream.close();
				
				String strPathIn = "./" + DateString + "/title/seoul/wordcount/" + DateString;
				String strPathOut = "./" + DateString + "/title/seoul/wordcount/" + DateString + "_result";				
				
				WordCount wc = new WordCount();
				try {
					//워드카운트
					wc.start(strPathIn, strPathOut, "seoul");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
			else if (newsName.equals("total")){
				
				//DateString = "20150513"; //임시
				
				String line;
				String sortWord;
				String keyWord;
				int i;

				//전체 신문 제목 처리
				Configuration conf = new Configuration();
				FileSystem hdfs = FileSystem.get(conf);
				
				Path path = new Path("./" + DateString + "/title/total/wordcount/" + DateString);
				FSDataOutputStream outStream = hdfs.create(path);
	
				outStream.writeUTF(totalDataTitleWord);
				outStream.close();
				
				//전체 신문 워드카운트
				String pathIn = "./" + DateString + "/title/total/wordcount/" + DateString;
				String pathOut = "./" + DateString + "/title/total/wordcount/" + DateString + "_result";
				
				System.out.println("========전체 신문 제목 워드카운트=========");
				WordCount wc = new WordCount();
				try {
					//워드카운트
					wc.start(pathIn, pathOut, "total");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/* 파일을 불러와서 string 값에 넣기 */				
				Path path2 = new Path("./" + DateString + "/title/total/wordcount/" + DateString + "_result/part-r-00000");
				FSDataInputStream inputStream = hdfs.open(path2);
				inputStream.seek(0);
				
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				//line = br.readLine();
				
				//BasicDBObject dbobj2 = new BasicDBObject();
				

				
				
				
				while ((line=br.readLine())!=null) {			
					MongoClient mongoClient = new MongoClient();
					DB db = mongoClient.getDB("unicl");
					DBCollection col1 = db.getCollection("newswid");
					
					Scanner sc = new Scanner(line.toString());
					String a = sc.next();
					int b = Integer.parseInt(sc.next());							
					if(a.length() > 2) {
						//dbobj2.append("word", a).append("count", b);
						
						//MongodbConn db = new MongodbConn();
						
						BasicDBObject dbobj = new BasicDBObject("code", DateString + "_result")
						.append("news", "total")
						//.append("wordcount", dbobj2);
						.append("word", a)
						.append("count", b);		
						//BasicDBObject dbobj = new BasicDBObject("code", "DateString + _result_2");
						col1.insert(dbobj);					
						//System.out.println(a + b);
					}
					sc.close();
					mongoClient.close();
				}				
				System.out.println("==================");
				
				
				/*
				if(line !=  null) {					
					//최종 제목 워드카운트에서 상위 20개만 연관단어 탐색
					for(i=0; i<1; i++) {
						//line = line.concat(line + "\n");
						
						Scanner sc = new Scanner(line);
						keyWord = sc.next();
						//System.out.println(line);
						
						//연관단어 가져오기
						RelativeWord rw = new RelativeWord("반기문", DateString);
						line = br.readLine();
					}
				}*/			
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws IOException{
		
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String DateString = format.format(now);
		
	   Unicl unicl = new Unicl();
	   
		//조선일보 너무 많아...
	   //unicl.run("chosun");
	   //경향신문 안됨
	   //unicl.run("khan");
	   //연합뉴스
	   //unicl.run("yonhap");
	   //한국경제
	   //unicl.run("hankyung");
	   //국민일보
	   unicl.run("kmib");
	   //한국일보
	   unicl.run("hankook");
	   //한겨례신문
	   unicl.run("hani");
	   //이데일리 안됨
	   unicl.run("edaily");
	   //데일리안 안됨
	   unicl.run("dailian");
	   //서울신문
	   unicl.run("seoul");
			
       //unicl.run("total");
		/* tf-idf */
		//Tfidf tfidf = new Tfidf(DateString);
		//tfidf.start3();
	
	   
	}

}

