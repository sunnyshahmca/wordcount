package org.sunny.wordcount;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.CombineFileSplit;
import org.apache.log4j.Logger;

public class CombinedTextInputFormatReader extends
		RecordReader<Text, NullWritable> {

	Logger logger = Logger.getLogger(CombinedTextInputFormatReader.class);
	private Path[] paths;	
	private Text key = new Text();
	RecordReaderInternal recordReaderInternal;
	public CombinedTextInputFormatReader(CombineFileSplit inputSplit, TaskAttemptContext taskContext) {
		paths = inputSplit.getPaths();
		recordReaderInternal = new RecordReaderInternal(paths, taskContext.getConfiguration());
		logger.info("******************************************************Start********************");
		int count = 1;
		for(Path path:paths) {
			logger.info(" [ " + count + " ] " + path.toString());
			count++;
		}
		logger.info("******************************************************End********************");
	}

	@Override
	public void close() throws IOException {
		recordReaderInternal.close();
		// TODO Auto-generated method stub
	}

	@Override
	public Text getCurrentKey() throws IOException,
			InterruptedException {
		return key;
	}

	@Override
	public NullWritable getCurrentValue() throws IOException, InterruptedException {
		return NullWritable.get();
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		return recordReaderInternal.getProgress();
	}

	@Override
	public void initialize(InputSplit arg0, TaskAttemptContext arg1)
			throws IOException, InterruptedException {

	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		boolean isReadSuccessful = recordReaderInternal.nextKeyValue();
		if(isReadSuccessful) {
//			long position = recordReaderInternal.getPosition();
//			key.set(position);
			String line = recordReaderInternal.getCurrentLine();
			key.set(line);
		}
		return isReadSuccessful;
	}

}
