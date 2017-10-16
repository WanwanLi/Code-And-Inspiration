import java.io.*;
import java.util.*;
public class JavaAndGauss_Legendre
{
	public static void main(String[] args)
	{
		Gauss_Legendre Gauss_Legendre1=new Gauss_Legendre();
		double a=0,b=1;
		for(int n=1;n<=10;n++)System.out.println("Gauss_Legendre1.S("+n+")="+Gauss_Legendre1.S(n,a,b));
	}
}
class Gauss_Legendre
{
	public double S(int n,double a,double b)
	{
		double G=0;
		if(n>10)n=10;
		double[] x=this.getXFromGauss_LegendreTable(n);
		double[] w=this.getWFromGauss_LegendreTable(n);
		for(int i=0;i<n;i++)G+=(b-a)/2*w[i]*f(x[i]*(b-a)/2+(b+a)/2);
		return G;
	}
	private double[] getXFromGauss_LegendreTable(int n)
	{
		double[] x=new double[n];	
		try
		{
			Scanner Scanner1=new Scanner(new File("Gauss_LegendreTable\\x.txt"));
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
	private double[] getWFromGauss_LegendreTable(int n)
	{
		double[] w=new double[n];	
		try
		{
			Scanner Scanner1=new Scanner(new File("Gauss_LegendreTable\\w.txt"));
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
		return 4/(1+x*x);
	}
}