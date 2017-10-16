import java.io.*;
public class JavaAndDataIO
{
	public static void main(String[] args)
	{
		testDoubleIO();
	}
	static void testIntIO()
	{
		int length=11;
		int[] data=new int[length];
		for(int i=0;i<length;i++)data[i]=i*i*i;
		writeInt("int.dat",data);
		data=readInt("int.dat");
		for(int i=0;i<length;i++)System.out.println(data[i]+"\t");
	}
	static void testDoubleIO()
	{
		int length=11;
		double[] data=new double[length];
		for(int i=0;i<length;i++)data[i]=i+0.01*i;
		writeDouble("double.dat",data);
		data=readDouble("double.dat");
		for(int i=0;i<length;i++)System.out.println(data[i]+"\t");
	}
	static void writeInt(String fileName,int[] data)
	{
		try
		{
			File file=new File(fileName);
     			DataOutputStream DataOutputStream1=new DataOutputStream(new FileOutputStream(file));
			for(int i=0;i<data.length;i++)DataOutputStream1.writeInt(data[i]);
			DataOutputStream1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	static int[] readInt(String fileName)
	{
		int[] data=null;
		int sizeof_Int=4;
		try
		{
			File file=new File(fileName);
			int dataLength=(int)file.length()/sizeof_Int;
			data=new int[dataLength];
     			DataInputStream DataInputStream1=new DataInputStream(new FileInputStream(file));
			for(int i=0;i<dataLength;i++)data[i]=DataInputStream1.readInt();
			DataInputStream1.close();
		}
		catch(Exception e){e.printStackTrace();}
		return data;
	}
	static void writeDouble(String fileName,double[] data)
	{
		try
		{
			File file=new File(fileName);
     			DataOutputStream DataOutputStream1=new DataOutputStream(new FileOutputStream(file));
			for(int i=0;i<data.length;i++)DataOutputStream1.writeDouble(data[i]);
			DataOutputStream1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	static double[] readDouble(String fileName)
	{
		double[] data=null;
		int sizeof_Double=8;
		try
		{
			File file=new File(fileName);
			int dataLength=(int)file.length()/sizeof_Double;
			data=new double[dataLength];
     			DataInputStream DataInputStream1=new DataInputStream(new FileInputStream(file));
			for(int i=0;i<dataLength;i++)data[i]=DataInputStream1.readDouble();
			DataInputStream1.close();
		}
		catch(Exception e){e.printStackTrace();}
		return data;
	}
}