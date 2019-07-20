package unicl;

import java.io.IOException;
import java.util.Scanner;
//import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper extends 
	Mapper<LongWritable, Text, Text, IntWritable> {
	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();
	
	public void map(LongWritable key, Text value, Context context)
		throws IOException, InterruptedException {
		Scanner sc = new Scanner(value.toString());
		while(sc.hasNext()) {
			word.set(sc.nextLine());
			context.write(word, one);
		}
		sc.close();
		/*
		StringTokenizer itr = new StringTokenizer(value.toString(), "\n");
		while (itr.hasMoreTokens()) {
			word.set(itr.nextToken());
			context.write(word, one);
		}*/
	}
}
