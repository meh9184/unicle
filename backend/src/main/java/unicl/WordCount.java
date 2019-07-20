package unicl;

/*
import java.text.SimpleDateFormat;
import java.util.Date;*/

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class WordCount {
	public void start(String pathIn, String pathOut, String NewsName) throws Exception {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "WordCount");
		
		job.setJarByClass(WordCount.class);
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);		
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		//FileInputFormat.addInputPath(job, new Path(path + DateString));
		//FileOutputFormat.setOutputPath(job, new Path(pathOut + "_result"));
		FileInputFormat.addInputPath(job, new Path(pathIn));
		FileOutputFormat.setOutputPath(job, new Path(pathOut));
	   
	    job.waitForCompletion(true);		
	}
}
