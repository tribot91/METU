import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Hw3 {
  // ------------------------------------------------------------------------------------- GameCount
  public static class GameCountMapper extends Mapper<Object, Text, Text, IntWritable>{
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        if(!word.toString().matches("^[0-9]*$"))
          context.write(word, one);
      }
    }
  }

  public static class Reducer1 extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values)
        sum += val.get();
      
      result.set(sum);
      context.write(key, result);
    }
  }
  // ------------------------------------------------------------------------------------- Point
  public static class PointMapper extends Mapper<Object, Text, Text, IntWritable>{
    private final static IntWritable one = new IntWritable(1);
    private final static IntWritable three = new IntWritable(3);
    private Text word = new Text(), 
                team1 = new Text(), 
                team2 = new Text();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());

      while (itr.hasMoreTokens()) {
        team1.set(itr.nextToken());
        team2.set(itr.nextToken());
        word.set(itr.nextToken());

        if(word.toString().equals("0")) {
          context.write(team1, one);
          context.write(team2, one);
        } else if (word.toString().equals("1")) {
          context.write(team1, three);
        } else if (word.toString().equals("2")) {
          context.write(team2, three);
        }
      }
    }
  }
  // ------------------------------------------------------------------------------------- Average
  public static class AvgMapper extends Mapper<Object, Text, Text, Text> {
    private Text word = new Text(), 
                team1 = new Text(), 
                team2 = new Text();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());

      while (itr.hasMoreTokens()) {
        team1.set(itr.nextToken());
        team2.set(itr.nextToken());
        word.set(itr.nextToken());

        if(word.toString().equals("0")) {
          context.write(team1, new Text("1-1"));
          context.write(team2, new Text("1-1"));
        } else if (word.toString().equals("1")) {
          context.write(team1, new Text("3-1"));
          context.write(team2, new Text("0-1"));
        } else if (word.toString().equals("2")) {
          context.write(team2, new Text("3-1"));
          context.write(team1, new Text("0-1"));
        }
      }
    }
  }

  public static class Reducer2 extends Reducer<Text, Text, Text, FloatWritable> {
    private FloatWritable result = new FloatWritable();

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
      float sum1 = 0, sum2= 0;
      for (Text val : values) {
        String[] arr = val.toString().split("-");
        sum1 += Float.valueOf(arr[0]);
        sum2 += Float.valueOf(arr[1]);
      }
      result.set(sum1/sum2);
      context.write(key, result);
    }
  }
  // ------------------------------------------------------------------------------------- Stat
  public static class StatMapper extends Mapper<Object, Text, Text, IntWritable>{
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());

      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        word.set(itr.nextToken());
        word.set(itr.nextToken());

        if(word.toString().equals("0")) {
          context.write(new Text("DRAW"), one);
        } else if (word.toString().equals("1")) {
          context.write(new Text("HOME_WIN"), one);
        } else if (word.toString().equals("2")) {
          context.write(new Text("AWAY_WIN"), one);
        }
      }
    }
  }

  public static class Reducer3 extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values)
        sum += val.get();
      
      result.set(sum);
      context.write(key, result);
    }
  }

  //Partitioner class
  public static class StatPartitioner extends Partitioner <Text, IntWritable> {
    public int getPartition(Text key, IntWritable value, int numReducer) {
      if (key.toString().equals("DRAW")) 
        return 0;
      else if (key.toString().equals("HOME_WIN"))
        return 1 % numReducer;
      else 
        return 2 % numReducer;
    }
  }


  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "CENG495 - HW3");
    job.setJarByClass(Hw3.class);
    FileInputFormat.addInputPath(job, new Path(args[1]));
    FileOutputFormat.setOutputPath(job, new Path(args[2]));

    if(args[0].equals("game")) {
      job.setMapperClass(GameCountMapper.class);
      job.setCombinerClass(Reducer1.class);
      job.setReducerClass(Reducer1.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);
    } else if(args[0].equals("point")) {
      job.setMapperClass(PointMapper.class);
      job.setCombinerClass(Reducer1.class);
      job.setReducerClass(Reducer1.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);
    } else if(args[0].equals("avg")) {
      job.setMapperClass(AvgMapper.class); 
      job.setReducerClass(Reducer2.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(Text.class);
    } else if(args[0].equals("stat")) {
      job.setNumReduceTasks(3);
      job.setMapperClass(StatMapper.class);
      job.setPartitionerClass(StatPartitioner.class);
      job.setReducerClass(Reducer3.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);
    }

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}