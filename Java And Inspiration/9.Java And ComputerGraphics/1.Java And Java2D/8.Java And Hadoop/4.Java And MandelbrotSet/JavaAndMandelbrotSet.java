import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
 
class Map extends Mapper<LongWritable, Text, Text, Text> 
{
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
	{
		Configuration conf = context.getConfiguration();
		int width = Integer.parseInt(conf.get("imageWidth"));
		int height = Integer.parseInt(conf.get("imageHeight"));
		double minX = Double.parseDouble(conf.get("minX"));
		double minY = Double.parseDouble(conf.get("minY"));
		double maxX = Double.parseDouble(conf.get("maxX"));
		double maxY = Double.parseDouble(conf.get("maxY"));
		String[] values = value.toString().split(",");
		int row = Integer.parseInt(values[0]);
		int col = Integer.parseInt(values[1]);
		double x = minX + col*(maxX-minX)/width;
		double y = minY +  (height-1-row)*(maxY-minY)/height;
		Text outputKey = new Text(row+ "," + col);
		Text outputValue = new Text(x+ "," + y);
		context.write(outputKey, outputValue);
	}
}
class Reduce extends Reducer<Text, Text, Text, Text> 
{
	private double mag2(double x,double y){return x*x+y*y;}
	private double dif2(double x,double y){return x*x-y*y;}
	private String getMandelbrotColor(double x,double y)
	{
		int nIters=0,maxIters=40;
		double newX=0.0,newY=0.0;
		while(mag2(newX,newY)<=4.0&&nIters<maxIters)
		{
			double sqx = dif2(newX,newY);
			double sqy = 2.0*newX*newY;
			newY = y+sqy;
			newX = x+sqx;
			nIters++;
		}
		int val =(int)(255.0*nIters/maxIters);
		return "0,"+val+",0,255";
	}
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException 
	{
		for (Text val : values) 
		{
			String k = key.toString();
			String[] value = val.toString().split(",");
			double x = Double.parseDouble(value[0]);
			double y = Double.parseDouble(value[1]);
			String pixel = getMandelbrotColor(x,y);
			Text output = new Text(k + "," +pixel);
			context.write(null, output);
		}
	}
}
public class JavaAndMandelbrotSet
{
	public static void main(String[] args) throws Exception 
	{
		Configuration conf = new Configuration();
		conf.set("imageWidth", "1250");
		conf.set("imageHeight", "750");
		conf.set("minX", "-2.0");
		conf.set("minY", "-1.0");
		conf.set("maxX", "1.0");
		conf.set("maxY", "1.0");
		Job job = new Job(conf, "JavaAndMandelbrotSet");
		job.setJarByClass(JavaAndMandelbrotSet.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
