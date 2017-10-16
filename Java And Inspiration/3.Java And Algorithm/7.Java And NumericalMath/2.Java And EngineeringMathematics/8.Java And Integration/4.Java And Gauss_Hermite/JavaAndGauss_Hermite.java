import java.io.*;
import java.util.*;
public class JavaAndGauss_Hermite
{
	public static void main(String[] args)
	{
		Gauss_Hermite Gauss_Hermite1=new Gauss_Hermite();
		for(int n=1;n<=5;n++)System.out.println("Gauss_Hermite1.S(-INF,+INF)="+Gauss_Hermite1.S(n));
	}
}
class Gauss_Hermite
{
	public double S(int n)
	{
		double G=0;
		if(n>5)n=5;
		double[] x=this.getXFromGauss_HermiteTable(n);
		double[] w=this.getWFromGauss_HermiteTable(n);
		for(int i=0;i<n;i++)G+=w[i]*f(x[i]);
		return G;
	}
	private double[] getXFromGauss_HermiteTable(int n)
	{
		double[] x=new double[n];	
		try
		{
			Scanner Scanner1=new Scanner(new File("Gauss_HermiteTable\\x.txt"));
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
	private double[] getWFromGauss_HermiteTable(int n)
	{
		double[] w=new double[n];	
		try
		{
			Scanner Scanner1=new Scanner(new File("Gauss_HermiteTable\\w.txt"));
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
		double fx=Math.exp(-x*x)*(Math.cos(x)+Math.pow(x,6));
		return Math.cos(x)+Math.pow(x,6);
	}
}