import java.io.*;
import java.util.*;
public class JavaAndLogisticRegression
{
	public static void main(String[] args)
	{
		test1();
	}
	public static void test0()
	{
		LogisticRegression logisticRegression1=new LogisticRegression();
		logisticRegression1.add(1,new double[]{1,2,1,3});
		logisticRegression1.add(1,new double[]{1,2,1,1});
		logisticRegression1.add(0,new double[]{2,1,3,1});
		logisticRegression1.add(1,new double[]{4,1,2,1});
		logisticRegression1.trainDataItems(1000,0.01);
		System.out.print("The Classify Result: P(X=1|{1,2,1,1})=");
		System.out.print(logisticRegression1.get(new double[]{1,2,1,1}));
	}
	public static void test1()
	{
		LogisticRegression logisticRegression1=new LogisticRegression();
		//logisticRegression1.genData("kr-vs-kp.data","Train.data","won");
		//logisticRegression1.genData("kr-vs-kp.data","Test.data","won");
		//logisticRegression1.genTest("Train.data");
		logisticRegression1.addTrainningData("Train.data");
		logisticRegression1.trainDataItems(1000,0.001);
		//The Learning Times_Rate(1000th, 0.001) The Miss Rate  is :15.7400688%
		//The Learning Times_Rate(1000/10000th, 0.01) The Miss Rate  is :22.7400688%
		System.out.println("The Test Results Are :");
		double missRate=100.0-logisticRegression1.testDataItems("Test.data");
		System.out.println("The Miss Rate is :"+missRate+"%");
	}
}
class DataItem
{
	public int label;
	public double[] attributes;
	public DataItem(int label, double[] attributes)
	{
		this.label=label;
		this.attributes=attributes;
	}
}
class LogisticRegression
{
	private double rate;
	private double[] weights;
	private int length= 1000;
	private ArrayList<DataItem> list;
	public LogisticRegression()
	{
		this.rate = 0.001;
		this.weights = new double[length];
		this.list=new ArrayList<DataItem>();
	}
	private double sigmoid(double z)
	{
		return 1 / (1 + Math.exp(-z));
	}
	public double get(double[] attributes)
	{
		double logit = 0.0;
		for (int i=0; i<attributes.length;i++)
		{
			logit += weights[i] *attributes[i];
		}
		return sigmoid(logit);
	}
	private void optimize(double label, double predict, double[] attributes)
	{
		for (int i=0;i<attributes.length; i++)
		{
			this.weights[i] += rate * (label - predict) * attributes[i];
		}
	}
	public double trainDataItems(int times, double rate)
	{
		this.rate=rate;
		double logLikelihood = 0.0;
		for (int t=0; t<times; t++)
		{
			for (int i=0; i<this.list.size(); i++)
			{
				DataItem item=this.list.get(i);
				double predict=this.get(item.attributes);
				this.optimize(item.label, predict, item.attributes);
				logLikelihood += item.label* Math.log(predict) + (1-item.label) * Math.log(1-predict);
			}
			if(t%100==0)
			System.out.println("Trainning Data Items "+t+"th Times.");
		}
		return logLikelihood;
	}
	public void add(int label, double[] attributes)
	{
		this.list.add(new DataItem(label,attributes));
	}
	public void add(String[] trainData)
	{
		double[] attributes=new double[trainData.length-1];
		for(int i=0;i<attributes.length;i++)attributes[i]=Double.parseDouble(trainData[i+1])-100;
		this.add(Integer.parseInt(trainData[0]), attributes);
	}
	public double get(String[] testData)
	{
		double[] attributes=new double[testData.length-1];
		for(int i=0;i<attributes.length;i++)attributes[i]=Double.parseDouble(testData[i+1])-100;
		return this.get(attributes);
	}
	public void addTrainningData(String fileName)
	{
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName)); 
			String line=BufferedReader1.readLine();
			while(line!=null)
			{
				this.add(line.split(","));
				line=BufferedReader1.readLine();
			}
			BufferedReader1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	public double testDataItems(String fileName)
	{
		try
		{
			int n=0,c=0;
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName)); 
			String line=BufferedReader1.readLine();n++;
			while(line!=null)
			{
				String[] testData=line.split(",");
				double predict=this.get(testData);
				System.out.println("label="+testData[0]+" pre="+predict);
				if(this.equals(predict,testData[0]))c++;
				else System.out.println(".... MISS");
				line=BufferedReader1.readLine();n++;
			}
			BufferedReader1.close();
			return 100.0*c/n;
		}
		catch(Exception e){e.printStackTrace();}
		return 100.0;
	}
	public void genData(String fileName, String dataName, String possitiveName)
	{
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName)); 
			PrintWriter PrintWriter1=new PrintWriter(dataName); 
			String line=BufferedReader1.readLine();
			while(line!=null)
			{
				String[] testData=line.split(",");
				int len=testData.length;
				boolean p=testData[len-1].equals(possitiveName);
				PrintWriter1.print(p?"1,":"0,");
				for(int i=0;i<len-2;i++)
				{
					int value=(int)testData[i].charAt(0);
					PrintWriter1.print(value+",");
				}
				int value=(int)testData[len-2].charAt(0);
				PrintWriter1.println(value);
				line=BufferedReader1.readLine();
			}
			BufferedReader1.close();
			PrintWriter1.close();
			System.out.println("Data: "+dataName+" is generated...");
		}
		catch(Exception e){e.printStackTrace();}
	}
	public double genTest(String fileName)
	{
		try
		{
			int n=0,c=0;
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName)); 
			String line=BufferedReader1.readLine();
			String[] lines=new String[5000];
			while(line!=null)
			{
				lines[n++]=line;
				line=BufferedReader1.readLine();

			}
			BufferedReader1.close();
			PrintWriter PrintWriter1=new PrintWriter("Gen."+fileName); 
			for(int i=0;i<n;i++)
			{
				String str=lines[(int)(Math.random()*(n-1))];
				PrintWriter1.println(str);
			}
			PrintWriter1.close();
		}
		catch(Exception e){e.printStackTrace();}
		return 100.0;
	}
	private boolean equals(double predict, String string)
	{
		return (Integer.parseInt(string)==1)?predict>0.4:predict<=0.4;
	}
}
