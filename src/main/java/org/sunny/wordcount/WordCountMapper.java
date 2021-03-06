package org.sunny.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper extends
		Mapper<Text, NullWritable, Text, IntWritable> {
	 
	enum CodeLine {
		    ZeroWords,
		    NonZeroWords
	 }
	public static String[] splitLines(String line) {
		return line.split("((?!_)\\p{Punct}|\\s)+",0);
	}
	@Override
	public void map(Text key, NullWritable value, Context context)
	      throws IOException, InterruptedException {		
	    String line = key.toString();
	    String[] words = splitLines(line);
	    int wordCounter = 0;
	    for(String word:words) {
	    	if(word.length() > 1) {
	    		++wordCounter;
	    		context.write(new Text(word), new IntWritable(1));
	    	}
	    }
	    if(wordCounter > 0) {
	    	context.getCounter(CodeLine.NonZeroWords).increment(1);
	    	context.getCounter("Code_Line", "Non_Zero_Words").increment(1);
	    } else {
	    	context.getCounter(CodeLine.ZeroWords).increment(1);
	    	context.getCounter("Code_Line", "Zero_Words").increment(1);
	    }
	  }
}
