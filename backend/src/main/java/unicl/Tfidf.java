package unicl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class Tfidf {

//	private String newsName;
	private String keyWord;
	private String DateString;

	public Tfidf(String keyWord, String DateString) {
		//this.newsName = newsName;
		this.keyWord = keyWord;
		this.DateString = DateString;
		}
	
	public Tfidf(String DateString) {
		this.DateString = DateString;
	}


    public static double MyRound(double num, int pos){
       
        // double temp = (int)((num+0.05)*10) / 10.0; // xxx.x
       
        //[1]
        double result = 0.0;
        double half = 0.5;
        double factor = 1;
       
        //[2]
        for (int i = 0; i< pos; i++){
            half *= 0.1;
            factor *= 10;
        }
        result = (int)((num+half)*factor)/(double)factor;
       
        //[3]      
        return result;     
    }	
	
	public void start() throws IOException {

		System.out.println("================TF-IDF==================");
		int x, y;
		int D;
		String line;
		Configuration conf = new Configuration();

		//뉴스사 찾기
		FileSystem hdfs = FileSystem.get(conf);
		Path path = new Path("./" + DateString + "/content/");

		FileStatus[] status = hdfs.listStatus(path);

		for(x=0; x<status.length; x++) {

			//기사 찾기
			FileSystem hdfs2 = FileSystem.get(conf);
			if(!status[x].getPath().getName().equals("total")) {

				System.out.println("기사찾기");
				//Path path2 = new Path("./" + DateString + "/" + keyWord + "/content/"
				//		+ status[x].getPath().getName() + "/wordcount/");
				Path path2 = new Path("./" + DateString + "/content/"
						+ status[x].getPath().getName() + "/wordcount/");
				System.out.println(path2);
				FileStatus[] status2 = hdfs2.listStatus(path2);
				System.out.println("status2.length : " + status2.length );			
				
				Word [][] words = new Word[status2.length][1000];				
				
				for(y=0; y<status2.length; y++) {
					words[y][0] = new Word();
				
					//기사 하나하나마다 오픈
					
					FileSystem hdfs3 = FileSystem.get(conf);

					Path path3 = new Path("./" + DateString + "/content/"
							+ status[x].getPath().getName() + "/wordcount/"
							+ status2[y].getPath().getName() + "/part-r-00000");
					
					System.out.println(status2[y].getPath().getName());
					words[y][0].fileName = status2[y].getPath().getName().toString();

					System.out.println("y : " + y);
					System.out.println(words[y][0].fileName);
					//if(y == 0) break;
					
					System.out.println(path3);
					FSDataInputStream inputStream = hdfs3.open(path3);
					inputStream.seek(0);

					BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
					line = br.readLine();
					
					//System.out.println(line);
					
					
					
					int z = 1;
					//for (int i=0; i < status2.length; i++) {
						while ((line=br.readLine())!=null) {			
							//System.out.println(line);
							Scanner sc = new Scanner(line.toString());
							//StringTokenizer st = new StringTokenizer(line, " ");
							//String a = st.nextToken();
							//String b = st.nextToken();
						  //  System.out.println(sc);
							String a = sc.next();
							System.out.println("a : " + a);
						    String b = sc.next();
						    System.out.println("b : " + b);
						    /*
						    if(b == "황준국(왼쪽)") {
						    	a = b;
						    	b = sc.next();
						    }*/
						    
							//System.out.println(a);
							//System.out.println(b);
							
							words[y][z] = new Word();
							words[y][z].word = a;
							words[y][z].freq = Integer.parseInt(b);
							z++;
							//sc.nextLine();
							sc.close();
						}	
						//words[y][0] = new Word();
						words[y][0].freq = z-1;							
					//}
				 }//기사 for문
				hdfs2.close();

					//모든 단어 수
					for (int i=0; i<status2.length; i++) {
						for	(int j=1; j<words[i][0].freq; j++) {
							words[i][0].sum += words[i][j].freq;							
						}
						//System.out.println(words[i][0].sum);
					}
					
					//System.out.println("tf값");
					//tf값
					for (int i=0; i<status2.length; i++) {
						for	(int j=1; j<words[i][0].freq; j++) {
							words[i][j].tf = (double)words[i][j].freq / (double)words[i][0].sum;
						}
					}			
							
					//단어 i가 포함된 문서 수
					for (int i=0; i<status2.length; i++) {
						for	(int j=1; j<words[i][0].freq; j++) {
							D = 0;
							for (int a=0; a<status2.length; a++) {
								for	(int b=1; b<words[a][0].freq; b++) {
									//System.out.print(words[i][j].word + " ");
									//System.out.println(words[x][y].word);							
									if (words[i][j].word.equals(words[a][b].word)) {								
										D++;
										//System.out.println("y : " + y);
										break;
									}
								}
								//System.out.println("x : " + x);
							}
							words[i][j].d = D;
							//System.out.println(words[i][j].word + " " + D);
						}
					}	
					
					//System.out.println("idf값");
					//idf 값
					for (int i=0; i<status2.length; i++) {
						for	(int j=1; j<words[i][0].freq; j++) {
							words[i][j].idf = Math.log10((double)(status2.length+1) / (double)words[i][j].d);
						}
					}	
					
					//System.out.println("tf-idf값");
					String strTfidf = "";
					for (int i=0; i<status2.length; i++) {
						System.out.println("i : " + i);
						for	(int j=1; j<words[i][0].freq; j++) {
							words[i][j].tfidf = words[i][j].tf * words[i][j].idf;
							strTfidf = strTfidf + words[i][j].word.toString() + " "
									+ words[i][j].tfidf + "\n";			
							//System.out.println(words[i][j].word + " " + words[i][j].tfidf);
						}
						System.out.println(strTfidf);
						//System.out.println("================save==============");
						//System.out.println(words[i][0].fileName);						
						System.out.println(words[i][0].fileName.toString());
						
						String wcPath = "./" + DateString + "/content/"
								+ status[x].getPath().getName() + "/tfidf/"
								+ words[i][0].fileName.toString();//status2[y].getPath().getName();
						
						//System.out.println(wcPath);
						HdfsFileSave hdfsSave = new HdfsFileSave();
						//System.out.println("==================================");
						hdfsSave.hdfsSave(wcPath, strTfidf);							
						
					}
								
					/*
					int u = 2;
					
				    for(int j=1; j<words[u][0].freq; j++) {
							System.out.print(words[u][j].word + " ");
							System.out.println(words[u][j].freq);
					}*/										
				//}//기사 for문
			}
		}//뉴스 for문
	}

	public void start2() throws IOException {
	}
	
	public void start3() throws IOException {
		System.out.println("================TF-IDF==================");
		int x, y;
		int D;
		String line;
		Configuration conf = new Configuration();
		
		//뉴스사 찾기
		FileSystem hdfs = FileSystem.get(conf);
		Path pathNews = new Path("./" + DateString + "/content/");
		FileStatus[] statusNews = hdfs.listStatus(pathNews);
		Word [][][] words = new Word[statusNews.length][300][1000];	
		
		for(x=0; x<statusNews.length; x++) {
			//기사 찾기
			FileSystem hdfs2 = FileSystem.get(conf);
			
			System.out.println("기사찾기");
			Path pathGisa = new Path("./" + DateString + "/content/" + statusNews[x].getPath().getName() + "/wordcount/");
			System.out.println(pathGisa);
			FileStatus[] statusGisa = hdfs2.listStatus(pathGisa);
			System.out.println("status2.length : " + statusGisa.length );		
			
			//if(!statusNews[x].getPath().getName().equals("total")) {
				
				for(y=0; y<statusGisa.length; y++) { //0은 sum 용이기때문에 +1을 더 시켜야된다
					System.out.println("y : " + y);
					words[x][y][0] = new Word();
					if(y==0) continue;
					//기사 하나하나마다 오픈
					
					FileSystem hdfs3 = FileSystem.get(conf);

					Path pathWord = new Path("./" + DateString + "/content/"
							+ statusNews[x].getPath().getName() + "/wordcount/"
							+ statusGisa[y].getPath().getName() + "/part-r-00000");
					
					System.out.println(statusGisa[y].getPath().getName());
					words[x][y][0].fileName = statusGisa[y].getPath().getName().toString();

					System.out.println("y : " + y);
					System.out.println(words[x][y][0].fileName);
					
					System.out.println("path3 : " + pathWord);
					FSDataInputStream inputStream = hdfs3.open(pathWord);
					inputStream.seek(0);

					BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
					line = br.readLine();
					
					System.out.println(words[x][y][0].fileName);
					
					line = br.readLine();
					
					int z = 1;
						while ((line=br.readLine())!=null) {			
							Scanner sc = new Scanner(line.toString());
							
							String a = sc.next();
							String b = sc.next();
							
							words[x][y][z] = new Word();
							words[x][y][z].word = a;
							words[x][y][z].freq = Integer.parseInt(b);
							z++;
							sc.close();
						}	
						words[x][y][0].freq = z-1; //z가 1로 시작하기 때문에 -1 해야됨(확정)
						System.out.println(words[x][y][0].fileName + " : " + words[x][y][0].freq);
						hdfs3.close();
				 }//기사 for문
			//}
				
				//모든 단어 수
				for(int j=1; j<statusGisa.length; j++) {	//0은 sum 용이기때문에 +1을 더 시켜야된다
					
						System.out.println("i : " + x);
						System.out.println("j : " + j);
						System.out.println(words[x][j][0].fileName);
						for (int k=1; k<words[x][j][0].freq; k++) {
							System.out.println("k : " + k + " " + words[x][j][k].sum);
							words[x][j][0].sum += words[x][j][k].freq;
						}
						//System.out.println(words[i][j][0].sum);
				}
					
					System.out.println("tf값");
					//tf값
						for(int j=1; j<statusGisa.length; j++) {
							for (int k=1; k<words[x][j][0].freq; k++) {
								words[x][j][k].tf = MyRound((double)words[x][j][k].freq / (double)words[x][j][0].sum, 6);
							}						
						}		
							
					//단어 i가 포함된 문서 수
						for	(int j=1; j<statusGisa.length; j++) {
							for (int k=1; k<words[x][j][0].freq; k++) {
								D = 0;
									for	(int b=1; b<statusGisa.length; b++) {
										for (int c=1; c<words[x][b][0].freq; c++) {											
											if (words[x][j][k].word.equals(words[x][b][c].word)) {		
												D++;										
												break;											
											}
										}
									}
								words[x][j][k].d = D;
							}
						}
							
					
					System.out.println("idf값");
					//idf 값
					//for (int i=0; i<status.length; i++) {
						for	(int j=1; j<statusGisa.length; j++) {
							for (int k=1; k<words[x][j][0].freq; k++) {
								words[x][j][k].idf = MyRound(Math.log10((double)(statusGisa.length+1) / (double)words[x][j][k].d), 6);
							}
						}
					//}	
					
					System.out.println("tf-idf값");
					String strTfidf = "";
					//for (int i=0; i<status.length; i++) {
						System.out.println("i : " + x);
						for	(int j=1; j<statusGisa.length; j++) {
							strTfidf = "";
							for (int k=1; k<words[x][j][0].freq; k++) {								
								String tmp = Double.toString(words[x][j][k].tf * words[x][j][k].idf);
								if(tmp.contains("E-4")) {
									tmp = tmp.replace("E-4", "");
									words[x][j][k].tfidf = MyRound(MyRound(Double.parseDouble(tmp), 3)/1000, 6);
								}
								else if(tmp.contains("E-5")) {
									tmp = tmp.replace("E-5", "");
									words[x][j][k].tfidf = MyRound(MyRound(Double.parseDouble(tmp), 3)/1000, 6);
								}
								else {
									words[x][j][k].tfidf = MyRound(Double.parseDouble(tmp), 6);
								}
								
								strTfidf = strTfidf + words[x][j][k].word.toString() + "\t"
									//+ words[i][j][k].tf + "\t"
									//+ words[i][j][k].idf + "\t"
									+ words[x][j][k].tfidf + "\n";			
								
								System.out.println("Mongo==============");
								//몽고 디비 객체생성
								MongoClient mongoClient = new MongoClient();
								DB db = mongoClient.getDB( "unicl" );
								DBCollection col1 = db.getCollection("newswid");	
								
								String title = "";
								FileSystem hdfs4 = FileSystem.get(conf);
								Path pathTitle = 	new Path("./" + DateString + "/title/"
										+ statusNews[x].getPath().getName() + "/crawler/"
										+ statusGisa[j].getPath().getName());
								FSDataInputStream inputStream2 = hdfs4.open(pathTitle);
								inputStream2.seek(0);

								BufferedReader br2 = new BufferedReader(new InputStreamReader(inputStream2));
								title = br2.readLine();
								title = title.replace("\u0000", "");
								inputStream2.close();
								br2.close();
								hdfs4.close();
								
								try{
								BasicDBObject dbobj = new BasicDBObject("code", words[x][j][0].fileName.toString())
									.append("date", DateString)
									.append("news", statusNews[x].getPath().getName())
									.append("title", title)
									.append("type", "tfidf")
									.append("word", words[x][j][k].word.toString())
									.append("tfidf", words[x][j][k].tfidf);
								
								System.out.println("Mongo insert==============");
								col1.insert(dbobj);
								} catch (Exception e) {
									e.printStackTrace();
								}			
								mongoClient.close();
								System.out.println("Mongo close==============");
							}	
							System.out.println("========================");
							System.out.println(words[x][j][0].fileName.toString());
							
							String wcPath = "./" + DateString + "/content/"
									+ statusNews[x].getPath().getName() + "/tfidf/"
									+ words[x][j][0].fileName.toString();
							
							HdfsFileSave hdfsSave = new HdfsFileSave();
							hdfsSave.hdfsSave(wcPath, strTfidf);	
											
						}
						hdfs2.close();
			
		}//뉴스 for문		
		hdfs.close();
	}
}

				

			

		

		

		

			

	

