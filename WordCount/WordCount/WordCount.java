import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import java.util.Date;
import java.text.SimpleDateFormat;

public class WordCount {

  /**
   * The Mapper class for word count. It extends Hadoop's Mapper class.
   */
  public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>{

    // A constant to represent a word count of one.
    private final static IntWritable one = new IntWritable(1);
    // A Text object to hold each word that is processed.
    private Text word = new Text();

    /**
     * The map method of the Mapper.
     * @param key Input key, not used in this example.
     * @param value A line of text from the input data.
     * @param context The context to write output to.
     */
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      // Tokenizing the line of text into words.
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        // Set the word as text.
        word.set(itr.nextToken());
        // Write the word with a count of one to the context.
        context.write(word, one);
      }
    }
  }

  /**
   * The Reducer class for word count. It extends Hadoop's Reducer class.
   */
  public static class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
    // A writable integer to hold the sum of counts.
    private IntWritable result = new IntWritable();

    /**
     * The reduce method of the Reducer.
     * @param key The word.
     * @param values The counts associated with the word.
     * @param context The context to write output to.
     */
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      int sum = 0;
      // Sum up the counts for the word.
      for (IntWritable val : values) {
        sum += val.get();
      }
      // Set the sum in result.
      result.set(sum);
      // Write the word and its total count to the context.
      context.write(key, result);
    }
  }

  /**
   * The main method to set up the MapReduce job.
   * @param args Command line arguments, expected two paths: input and output directory.
   */
  public static void main(String[] args) throws Exception {
    // Setting up Hadoop MapReduce job configuration.
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    // Setting the class.
    job.setJarByClass(WordCount.class);
    // Setting the Mapper class.
    job.setMapperClass(TokenizerMapper.class);
    // Setting the combiner class, which is the Reducer used during the Map phase.
    job.setCombinerClass(IntSumReducer.class);
    // Setting the Reducer class.
    job.setReducerClass(IntSumReducer.class);
    // Setting the output key and value classes.
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    // Setting the paths for input and output directories.
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    // Exit the program when the job is finished.
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}