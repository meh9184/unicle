package unicl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class RelativeWord {
	private String newsName;
	private String keyWord;
	private String DateString;
	
	private void FindNews() throws IOException {
		int i, j;
		String line;
		Configuration conf = new Configuration();
		
		//뉴스사 찾기
		FileSystem hdfs = FileSystem.get(conf);
		Path path = new Path("./" + DateString + "/title");
		FileStatus[] status = hdfs.listStatus(path);
		
		for(i=0; i<status.length; i++) {
			//기사 찾기
			FileSystem hdfs2 = FileSystem.get(conf);
			
			if(!status[i].getPath().getName().equals("total")) {
				Path path2 = new Path("./" + DateString 
						+ "/title/" + status[i].getPath().getName() + "/crawler/");
				
				FileStatus[] status2 = hdfs2.listStatus(path2);
				
				for(j=0; j<status2.length; j++) {
					//기사 하나하나마다 오픈
					FileSystem hdfs3 = FileSystem.get(conf);
					Path path3 = new Path("./" + DateString + "/title/"
							+ status[i].getPath().getName() + "/crawler/"
							+ status2[j].getPath().getName());
					System.out.println(path3);
					
					FSDataInputStream inputStream = hdfs3.open(path3);
					inputStream.seek(0);
					
					
					BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
					line = br.readLine();								
					
					if(line !=  null) {	
						//기사 제목에 키워드가 포함되어있다면 해당 기사를 키워드 하위 디렉토리로 복사
						if(line.contains(keyWord)) {
							//키워드가 포함된 기사 제목이 복사 될 경로
							Path pathCrawler = new Path("./" + DateString + "/" + keyWord
									+ "/title/" + status[i].getPath().getName() + "/crawler/"
									+ status2[j].getPath().getName());
							
							//키워드가 포함된 기사 내용 경로
							Path pathLoadContent = new Path("./" + DateString
									+ "/content/" + status[i].getPath().getName() + "/crawler/"
									+ status2[j].getPath().getName());
							
							//키워드가 포함된 기사 형태소 분석 내용 경로
							Path pathLoadContentHannanum = new Path("./" + DateString
									+ "/content/" + status[i].getPath().getName() + "/hannanum/"
									+ status2[j].getPath().getName());
							
							//키워드가 포함된 기사 내용이 복사 될 경로
							Path pathSaveConten = new Path("./" + DateString + "/" + keyWord
									+ "/content/" + status[i].getPath().getName() + "/cralwer/"
									+ status2[j].getPath().getName());
							
							//키워드가 포함된 기사 형태소 분석 내용이 복사 될 경로
							Path pathSaveContentHannanum = new Path("./" + DateString + "/" + keyWord
									+ "/content/" + status[i].getPath().getName() + "/hannanum/"
									+ status2[j].getPath().getName());																	
							
							//키워드가 포함된 기사 내용 워드카운트
							Path pathLoadWordCount = new Path("./" + DateString
									+ "/content/" + status[i].getPath().getName() + "/wordcount/"
									+ status2[j].getPath().getName() + "/part-r-00000");							
							
							//키워드가 포함된 기사 내용 워드카운트가 복사 될 경로
							Path pathSaveWordCount = new Path("./" + DateString + "/" + keyWord
									+ "/content/" + status[i].getPath().getName() + "/wordcount/"
									+ status2[j].getPath().getName());
							
							//복사 임시 하드 경로
							Path pathLocal = new Path("./temp/ + " + status[i].getPath().getName() + 
									"/wordcount/" + status2[j].getPath().getName());
							System.out.println(line);
							
							//제목 복사
							FileSystem hdfs4 = FileSystem.get(conf);														
							FSDataOutputStream outStream = hdfs4.create(pathCrawler);
							outStream.writeUTF(line);
							outStream.close();
							
							System.out.println("제목 복사 시작");
							hdfs4.copyToLocalFile(pathLoadWordCount, pathLocal);
							hdfs4.copyFromLocalFile(true, pathLocal, pathSaveWordCount);
							
							
							System.out.println(pathLoadWordCount + " 복사완료");
						}														
					}
					br.close();		
					

							
				}
				/* tf-idf */
				Tfidf tfidf = new Tfidf(keyWord, DateString);
				tfidf.start();
				
			}
		}
		
		
		
	}
	public RelativeWord(String keyWord, String DateString) 
			throws IOException {
		//this.newsName = newsName;
		this.keyWord = keyWord;
		this.DateString = DateString;
		
		FindNews();
	}
}
