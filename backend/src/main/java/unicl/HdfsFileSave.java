package unicl;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsFileSave {
	public void hdfsSave (String strPath, String data) throws IOException{
		System.out.println("==========저장시작==============");
		Configuration conf = new Configuration();
		FileSystem hdfs = FileSystem.get(conf);
		
		//content20150406_3 이런식으로 저장 됨
		//Path path = new Path("./crawler/yonhap/title/" + DateString + "_" + count3);
		Path path = new Path(strPath);
		System.out.println(strPath);
		FSDataOutputStream outStream = hdfs.create(path);
		
		System.out.println(data.length());
		if(data.length() > 50000) {
			System.out.println("too long");
			//String subData = data.substring(1, 29999);
			//[outStream.writeUTF(subData);	
			//outStream.close();
			//String subData2 = data.substring(30000);
			//outStream.writeUTF(subData2);
			//outStream.close();
		}
		//else {
			outStream.writeUTF(data);
			outStream.close();
		//}
		
		System.out.println(strPath + " 저장완료");
		hdfs.close();
		conf.clear();
		//System.out.println("연합" + count + " " + url);
	}
}
