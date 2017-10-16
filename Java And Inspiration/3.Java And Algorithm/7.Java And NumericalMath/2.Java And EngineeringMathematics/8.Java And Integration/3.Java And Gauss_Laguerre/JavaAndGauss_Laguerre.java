import java.io.*;
import java.util.*;
public class JavaAndGauss_Laguerre
{
	public static void main(String[] args)
	{
		Gauss_Laguerre Gauss_Laguerre1=new Gauss_Laguerre();
		for(int n=1;n<=5;n++)System.out.println("Gauss_Laguerre1.S(0,+INF)="+Gauss_Laguerre1.S(n));
	}
}
class Gauss_Laguerre
{
	public double S(int n)
	{
		double G=0;
		if(n>5)n=5;
		double[] x=this.getXFromGauss_LaguerreTable(n);
		double[] w=this.getWFromGauss_LaguerreTable(n);
		for(int i=0;i<n;i++)G+=w[i]*f(x[i]);
		return G;
	}
	private double[] getXFromGauss_LaguerreTable(int n)
	{
		double[] x=new double[n];	
		try
		{
			Scanner Scanner1=new Scanner(new File("Gauss_LaguerreTable\\x.txt"));
			double d=Scanner1.nextDouble();
			d=Scanner1.nextDouble();
			while(Scanner1.hasNext()&&d!=n)d=Scanner1.nextDouble();
			d=Scanner1.nextDouble();
			for(int i=0;i<n;i++)
			{
				x[i]=d;
				d=Scanner1.nextDouble();
			}
		}
		catch(Exception e){}
		return x;
	}
	private double[] getWFromGauss_LaguerreTable(int n)
	{
		double[] w=new double[n];	
		try
		{
			Scanner Scanner1=new Scanner(new File("Gauss_LaguerreTable\\w.txt"));
			double d=Scanner1.nextDouble();
			d=Scanner1.nextDouble();
			while(Scanner1.hasNext()&&d!=n)d=Scanner1.nextDouble();
			d=Scanner1.nextDouble();
			for(int i=0;i<n;i++)
			{
				w[i]=d;
				d=Scanner1.nextDouble();
			}
		}
		catch(Exception e){}
		return w;
	}
	private double f(double x)
	{
		double fx=Math.exp(-x)*Math.sqrt(x);
		return Math.sqrt(x);
	}
}